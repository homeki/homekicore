package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.main.InternalData;

public class HttpRequestReceiverThread extends Thread {
	private HttpApi api = null;
	private Socket connectedClient = null;
	
	public HttpRequestReceiverThread(Socket client, HttpApi a) {
		connectedClient = client;
		api = a;
	}
	
	enum Type {
		GET, SET;
	}
	
	enum Command {
		OFF, ON, DIM, STATUS, DEBUGPRINT, DEVICES;
	}
	
	public void run() {
		String httpQueryString = "";
		BufferedReader in;
		DataOutputStream out;
		try {
			in = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			out = new DataOutputStream(connectedClient.getOutputStream());
			
			try {
				String headerLine = in.readLine();
				if (!headerLine.endsWith(" HTTP/1.1")) {
					HttpMacro.send404(headerLine, out);
				} else {
					headerLine = headerLine.substring(0, headerLine.length() - 9);
					
					StringTokenizer st = new StringTokenizer(headerLine, "/ ");
					
					boolean result = false;
					if (st.hasMoreTokens()) {
						String token = st.nextToken();
						if (token.equals("GET")) {
							result = HttpGetResolver.resolve(st,api, out);
						} else if (token.equals("SET")) {
							result = HttpSetResolver.resolve(st,api, out);
						}
					}
					if (!result) {
						HttpMacro.send404(headerLine, out);
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				HttpMacro.send404(httpQueryString, out);
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
	}
}
