package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;
import com.homekey.core.log.L;
import com.homekey.core.storage.ITableFactory;
import com.homekey.core.storage.sqlite.SqliteTableFactory;

public class ThreadMaster {
	private Monitor monitor;
	private LinkedList<ControlledThread> threads;
	private HttpApi api;
	private ITableFactory dbf;
	
	public ThreadMaster() {
		start();
	}
	
	private void start() {
		threads = new LinkedList<ControlledThread>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		dbf = new SqliteTableFactory("sqlite.db");
		
		// create all threads
		threads.add(new DetectorThread(monitor, dbf));
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
	
	public void restart() {
		L.i("Doing forced restart. Shutting down threads.");
		shutdown();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		L.i("Starting threads.");
		start();
		
	}
}
