package com.homekey.core.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.homekey.core.threads.ControlledThread;

public class HttpRequestResolverThread extends ControlledThread {
	private HttpApi api = null;
	private Socket connectedClient = null;

	public HttpRequestResolverThread(Socket client, HttpApi a) {
		super(0);
		quiet();
		connectedClient = client;
		api = a;
	}

	@Override
	public void iteration() throws InterruptedException {
		BufferedReader in;
		DataOutputStream out;

		try {
			in = new BufferedReader(new InputStreamReader(
					connectedClient.getInputStream()));
			out = new DataOutputStream(connectedClient.getOutputStream());
			String requestString = in.readLine();
			handleRequest(requestString, out,api);
		} catch (IOException e) {
			e.printStackTrace();
		}
		shutdown();
	}

	public static void handleRequest(String requestString, DataOutputStream out, HttpApi api)
			throws IOException {

		if (!requestString.endsWith(" HTTP/1.1")) {
			HttpMacro.send404(requestString, out);
		} else {
			requestString = requestString.replace(" HTTP/1.1", "");
			StringTokenizer st = new StringTokenizer(requestString, "/ ?&");
			if (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equals("GET")) {
					HttpGetResolver.resolve(st, api, out);
				} else if (token.equals("SET")) {
					HttpSetResolver.resolve(st, api, out);
				} else {
					HttpMacro.send404(requestString, out);
				}
			}
		}
	}

}
