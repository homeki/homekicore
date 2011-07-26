package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.main.Monitor;

public class HttpRequestResolverThread extends Thread {
	
	static final String HTML_START = "<html>" + "<title>HTTP Server in java</title>" + "<body>";
	
	static final String HTML_END = "</body>" + "</html>";
	
	Monitor monitor = null;
	Socket connectedClient = null;
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;
	
	public HttpRequestResolverThread(Socket client, Monitor m) {
		connectedClient = client;
		monitor = m;
	}
	
	public void run() {
		try {
			inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			outToClient = new DataOutputStream(connectedClient.getOutputStream());
			
			String requestString = inFromClient.readLine();
			String headerLine = requestString;
			
			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			String httpQueryString = tokenizer.nextToken();
			
			if (httpMethod.equals("GET")) {
				if (httpQueryString.equals("/")) {
					sendResponse(200, "Welcome to HomeKey, plx send command or nothing will happen");
				} else {
					System.out.println(httpQueryString);
					String[] query = httpQueryString.split("/");
					if (query[1].equals("get")) {
						if (query[2].equals("status")) {
							int id = Integer.parseInt(query[3]);
							Queryable q = (Queryable) monitor.getDevice(id);
							sendResponse(200, monitor.getStatus(q));
						} else if (query[2].equals("devices")) {
							sendResponse(200, monitor.getDevices());
						} else {
							throw new Exception();
						}
					} else if (query[1].equals("set")) {
						int id = Integer.parseInt(query[3]);
						Switchable s = (Switchable) monitor.getDevice(id);
						if (query[2].equals("on")) {
							sendResponse(200, monitor.turnOn(s));
						} else if (query[2].equals("off")) {
							sendResponse(200, monitor.turnOff(s));
						} else {
							throw new Exception();
						}
					} else {
						throw new Exception();
					}
				}
			} else {
				throw new Exception();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
			try {
				sendResponse(500, "Device is not Queryable!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				sendResponse(404, "lulul");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
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
		
		responseString = HTML_START + responseString + HTML_END;
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
