package com.homekey.core.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.main.InternalData;

public class HttpListenerThread extends Thread {
	private HttpApi api;

	public HttpListenerThread(HttpApi a) {
		api = a;
	}
	
	public void run() {
		ServerSocket Server;
		try {
			Server = new ServerSocket(5000, 10, null);
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