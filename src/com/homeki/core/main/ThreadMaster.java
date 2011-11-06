package com.homeki.core.main;

import java.io.FileNotFoundException;
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
	private ConfigurationFile file;
	
	public ThreadMaster() {
		addShutdownHook();
	}
	
	private void addShutdownHook() {
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread() { public void run() { shutdown(); }; });
	}
	
	public void launch() {
		file = new ConfigurationFile();
		threads = new LinkedList<ControlledThread>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		dbf = new SqliteTableFactory("homeki.db");
		
		try {
			file.load();
		} catch (FileNotFoundException ex) {
			L.e("Could not find configuration file, killing Homeki.");
			return;
		} catch (Exception ex) {
			L.e("Exception when parsing configuration file.", ex);
			return;
		}
		
		dbf.ensureTables();
		
		// create detector thread
		DetectorThread dthread = new DetectorThread(monitor, dbf);
		dthread.configure(file);
		threads.add(dthread);
		
		// create http listener thread
		try {
			threads.add(new HttpListenerThread(api));
		} catch (Exception e) {
			L.e("Could not bind socket for HttpListenerThread, killing Homeki.");
			return;
		}
		
		// create collector thread
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
