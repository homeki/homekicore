package com.homekey.core.http;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpMacro {
	public static void send404(String httpQueryString, DataOutputStream out) {
		try {
			sendResponse(404, httpQueryString + " does not exist", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendResponse(int statusCode, String responseString, DataOutputStream out) throws IOException {
		
		String statusLine = null;
		String serverdetails = "Server: homeKey Java HTTPServer";
		String contentLengthLine = null;
		String contentTypeLine = "Content-Type: text/html" + "\r\n";
		
		if (statusCode == 200)
			statusLine = "HTTP/1.1 200 OK" + "\r\n";
		else
			statusLine = "HTTP/1.1 404 Not Found" + "\r\n";
		
		contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
		
		out.writeBytes(statusLine);
		out.writeBytes(serverdetails);
		out.writeBytes(contentTypeLine);
		out.writeBytes(contentLengthLine);
		out.writeBytes("Connection: close\r\n");
		out.writeBytes("\r\n");
		
		out.writeBytes(responseString);
		
		out.close();
	}
}
