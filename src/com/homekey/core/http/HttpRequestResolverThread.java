package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequestResolverThread extends Thread {
	private HttpApi api = null;
	private Socket connectedClient = null;
	
	public HttpRequestResolverThread(Socket client, HttpApi a) {
		connectedClient = client;
		api = a;
	}
	
	enum Type {
		GET, SET;
	}
	
	enum Command {
		OFF, ON, DIM, STATUS, DEVICES;
	}
	
	public void run() {
		String httpQueryString = "";
		BufferedReader in;
		DataOutputStream out;
		
		try {
			in = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			out = new DataOutputStream(connectedClient.getOutputStream());
			
			String requestString = in.readLine();
			if (!requestString.endsWith(" HTTP/1.1")) {
				HttpMacro.send404(requestString, out);
			} else {
				requestString = requestString.replace(" HTTP/1.1", "");
				StringTokenizer st = new StringTokenizer(requestString, "/ ?&");
				if (st.hasMoreTokens()) {
					String token = st.nextToken();
					if(token.equals("GET")){
						HttpGetResolver.resolve(st,api, out);
					}else if(token.equals("SET")){
						HttpGetResolver.resolve(st,api, out);
					}else{
						HttpMacro.send404(httpQueryString, out);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
