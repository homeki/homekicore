package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.CommandsThread;
import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;

public class ThreadMaster {
	private CommandQueue queue;
	private InternalData data;
	private LinkedList<Thread> threads;
	private HttpApi api;
	
	public ThreadMaster() {
		// Thread holder
		threads = new LinkedList<Thread>();
		
		// Create global monitor
		data = new InternalData();
		
		queue = new CommandQueue();

		// Create HttpApi Interface
		api = new HttpApi(queue);
		
		// Create all threads.
		threads.add(new DetectorThread(queue));
		threads.add(new HttpListenerThread(api));
		threads.add(new CommandsThread(data, queue));
		
		for (Thread t : threads)
			t.start();
	}
	
	public InternalData getMonitor() {
		return data;
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
