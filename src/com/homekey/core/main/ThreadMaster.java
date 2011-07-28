package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.impl.SqliteDatabase;

public class ThreadMaster {
	private Monitor monitor;
	private Database db;
	private LinkedList<Thread> threads;
	private HttpApi api;
	
	public ThreadMaster() {
		threads = new LinkedList<Thread>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		db = new SqliteDatabase();
		
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
