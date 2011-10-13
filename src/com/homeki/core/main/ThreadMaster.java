package com.homeki.core.main;

import java.util.LinkedList;

import com.homeki.core.http.HttpApi;
import com.homeki.core.http.HttpListenerThread;
import com.homeki.core.log.L;
import com.homeki.core.storage.ITableFactory;
import com.homeki.core.storage.sqlite.SqliteTableFactory;
import com.homeki.core.threads.CollectorThread;
import com.homeki.core.threads.ControlledThread;
import com.homeki.core.threads.DetectorThread;

public class ThreadMaster {
	private Monitor monitor;
	private LinkedList<ControlledThread> threads;
	private HttpApi api;
	private ITableFactory dbf;
	
	public ThreadMaster() {
		addShutdownHook();
	}
	
	private void addShutdownHook() {
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread() { public void run() { shutdown(); }; });
	}
	
	public void launch() {
		threads = new LinkedList<ControlledThread>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		dbf = new SqliteTableFactory("homeki.db");
		
		dbf.ensureTables();
		
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
		launch();
		
	}
}
