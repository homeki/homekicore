package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;
import com.homekey.core.log.L;

public class ThreadMaster {
	private Monitor monitor;
	private LinkedList<ControlledThread> threads;
	private HttpApi api;
	
	public ThreadMaster() {
		threads = new LinkedList<ControlledThread>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		
		// create all threads
		threads.add(new DetectorThread(monitor));
		try {
			threads.add(new HttpListenerThread(api));
		} catch (Exception e) {
			L.e("Could not start HttpListenerThread.");
		}
		threads.add(new CollectorThread(monitor));
		
		for (Thread t : threads)
			t.start();
	}
	
	public void shutdown() {
		L.i("ThreadMaster shutting down threads...");
		for (ControlledThread t : threads)
			t.shutdown();
	}
}
