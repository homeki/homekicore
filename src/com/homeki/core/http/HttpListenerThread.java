package com.homeki.core.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.homeki.core.log.L;
import com.homeki.core.threads.ControlledThread;

public class HttpListenerThread extends ControlledThread {
	private HttpApi api;
	private ServerSocket Server;
	
	public HttpListenerThread(HttpApi a) throws IOException {
		super(0);
		api = a;
		Server = new ServerSocket(5000, 10, null);
	}
	
	@Override
	public void iteration() throws InterruptedException {
		try {
			Socket connected;
			connected = Server.accept();
			new HttpRequestResolverThread(connected, api).start();
		} catch (SocketException e) {
			L.i("Closed socket.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		try {
			Server.close();
		} catch (IOException e) {}

	}
}