package com.homekey.core.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.homekey.core.main.ControlledThread;

public class HttpListenerThread extends ControlledThread {
	private HttpApi api;
	ServerSocket Server;
	
	public HttpListenerThread(HttpApi a) throws IOException {
		super(0);
		api = a;
		Server = new ServerSocket(5000, 10, null);
	}
	
	@Override
	public void iteration() throws InterruptedException {
		try {
			while (true) {
				Socket connected;
				connected = Server.accept();
				new HttpRequestResolverThread(connected, api).start();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException("You suck!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}