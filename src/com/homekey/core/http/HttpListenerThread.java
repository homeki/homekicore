package com.homekey.core.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.homekey.core.main.Monitor;

public class HttpListenerThread extends Thread {
	
	private Monitor monitor;

	public HttpListenerThread(Monitor m) {
		monitor = m;
	}
	
	public void run() {
		
		ServerSocket Server;
		try {
			Server = new ServerSocket(5000, 10, null);
			while (true) {
				Socket connected;
				connected = Server.accept();
				new HttpRequestResolverThread(connected, monitor).start();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException("You suck!");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}