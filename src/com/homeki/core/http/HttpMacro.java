package com.homeki.core.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

import com.homeki.core.log.L;

public class HttpMacro {
	public static void send404(String httpQueryString, DataOutputStream out) {
		try {
			sendResponse(404, httpQueryString + " does not exist", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendResponse(int statusCode, String responseString,
			DataOutputStream out) throws IOException {

		String statusLine = null;
		String serverdetails = "Server: Homeki Java HTTP Server";
		String contentLengthLine = null;
		String contentTypeLine = "Content-Type: text/html" + "\r\n";

		if (statusCode == 200)
			statusLine = "HTTP/1.1 200 OK" + "\r\n";
		else
			statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

		contentLengthLine = "Content-Length: " + responseString.length()
				+ "\r\n";
		try {

			out.writeBytes(statusLine);
			out.writeBytes(serverdetails);
			out.writeBytes(contentTypeLine);
			out.writeBytes(contentLengthLine);
			out.writeBytes("Connection: close\r\n");
			out.writeBytes("\r\n");

			out.writeBytes(responseString);
		} catch (SocketException e) {
			L.w("Socket crashed while sending response: " + e.getMessage());
		}
		out.close();
	}

	public static void send405(String info, DataOutputStream out) {
		try {
			sendResponse(405, info, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
