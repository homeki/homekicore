package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.http.HttpListenerThread;

public class ThreadMaster {
	Monitor monitor;
	LinkedList<Thread> threads;
	
	public ThreadMaster() {
		threads = new LinkedList<Thread>();
		
		monitor = new Monitor();
		
		threads.add(new HttpListenerThread(monitor));
		threads.add(new CommandsThread(monitor));
		
		for (Thread t : threads)
			t.start();
	}
	
	public Monitor getMonitor() {
		return monitor;
	}
	
	public void shutdown() {
		System.out.println("Shutting down threads..");
		for (Thread t : threads)
			t.interrupt();
	}
}
