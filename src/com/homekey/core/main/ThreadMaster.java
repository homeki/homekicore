package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;

public class ThreadMaster {
	Monitor monitor;
	LinkedList<Thread> threads;
	private HttpApi api;
	
	public ThreadMaster() {
		// Thread holder
		threads = new LinkedList<Thread>();
		
		// Create global monitor
		monitor = new Monitor();
		
		// Create HttpApi Interface
		api = new HttpApi(monitor);
		
		// Create all threads.
		threads.add(new HttpListenerThread(monitor));
		threads.add(new CommandsThread(monitor));
		
		for (Thread t : threads)
			t.start();
	}
	
	public Monitor getMonitor() {
		return monitor;
	}
	
	public HttpApi getApi(){
		return api;
	}
	
	public void shutdown() {
		System.out.println("Shutting down threads..");
		for (Thread t : threads)
			t.interrupt();
	}
}
