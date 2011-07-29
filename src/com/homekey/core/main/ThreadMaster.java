package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;

public class ThreadMaster {
	private Monitor monitor;
	private LinkedList<Thread> threads;
	private HttpApi api;
	
	public ThreadMaster() {
		threads = new LinkedList<Thread>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		
		// create all threads
		threads.add(new DetectorThread(monitor));
		threads.add(new HttpListenerThread(api));
		threads.add(new CollectorThread(monitor));
		
		for (Thread t : threads)
			t.start();
	}
	
	public void shutdown() {
		System.out.println("Shutting down threads..");
		for (Thread t : threads)
			t.interrupt();
	}
}
