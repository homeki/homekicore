package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.main.Monitor;

public class HttpRequestResolverThread extends Thread {
	Monitor monitor = null;
	Socket connectedClient = null;
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;
	
	public HttpRequestResolverThread(Socket client, Monitor m) {
		connectedClient = client;
		monitor = m;
	}
	
	enum Type {
		GET, SET;
	}
	
	enum Command {
		OFF, ON, DIM, STATUS, DEVICES;
	}
	
	public void run() {
		String httpQueryString = "";
		try {
			inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			
			outToClient = new DataOutputStream(connectedClient.getOutputStream());
			
			String requestString = inFromClient.readLine();
			String headerLine = requestString;
			
			httpQueryString = gethttpQueryString(headerLine);
			Map<String, String> map = getQueryMap(httpQueryString);
			
			int id = -1;
			try {	
				id = Integer.valueOf(map.get("id"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			Type type = Type.valueOf(map.get("type").toUpperCase());
			Command command = Command.valueOf(map.get("command").toUpperCase());
			
			switch (type) {
			case GET:
				switch (command) {
				case STATUS:
					sendStatus(id);
					break;
				case DEVICES:
					sendDevices();
					break;
				default:
					send404(httpQueryString);
					break;
				}
				break;
			case SET:
				switch (command) {
				case OFF:
					setAndSendSwitch(id, false);
					break;
				case ON:
					setAndSendSwitch(id, true);
					break;
				default:
					send404(httpQueryString);
					break;
				}
				break;
			default:
				send404(httpQueryString);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
			send404(httpQueryString);
		}
	}
	
	private String gethttpQueryString(String headerLine) {
		StringTokenizer tokenizer = new StringTokenizer(headerLine);
		// String httpMethod =
		tokenizer.nextToken();
		String httpQueryString = tokenizer.nextToken();
		return httpQueryString;
	}
	
	private Map<String, String> getQueryMap(String httpQueryString) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String query = httpQueryString.split("\\?")[1];
			String[] kvp = query.split("&");
			for (String s : kvp) {
				String[] whatever = s.split("=");
				map.put(whatever[0], whatever[1]);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// Fulhaxx
			e.printStackTrace();
		}
		return map;
	}
	
	private void sendDevices() throws IOException {
		sendResponse(200, monitor.getDevices());
	}
	
	private void setAndSendSwitch(int id, boolean on) throws IOException {
		Switchable s = (Switchable) monitor.getDevice(id);
		sendResponse(200, String.valueOf(monitor.flip(s, on)));
	}
	
	private void send404(String httpQueryString) {
		try {
			sendResponse(404, httpQueryString + " does not exsist");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendStatus(int id) throws IOException {
		Queryable q = (Queryable) monitor.getDevice(id);
		sendResponse(200, monitor.getStatus(q));
	}
	
	public void sendResponse(int statusCode, String responseString) throws IOException {
		
		String statusLine = null;
		String serverdetails = "Server: Java HTTPServer";
		String contentLengthLine = null;
		String contentTypeLine = "Content-Type: text/html" + "\r\n";
		
		if (statusCode == 200)
			statusLine = "HTTP/1.1 200 OK" + "\r\n";
		else
			statusLine = "HTTP/1.1 404 Not Found" + "\r\n";
		
		contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
		
		outToClient.writeBytes(statusLine);
		outToClient.writeBytes(serverdetails);
		outToClient.writeBytes(contentTypeLine);
		outToClient.writeBytes(contentLengthLine);
		outToClient.writeBytes("Connection: close\r\n");
		outToClient.writeBytes("\r\n");
		
		outToClient.writeBytes(responseString);
		
		outToClient.close();
	}
}
