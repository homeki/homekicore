package com.homekey.core.main;

import java.util.LinkedList;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.CommandThread;
import com.homekey.core.http.HttpApi;
import com.homekey.core.http.HttpListenerThread;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.impl.SqliteDatabase;

public class ThreadMaster {
	private CommandQueue queue;
	private InternalData data;
	private Database db;
	private LinkedList<Thread> threads;
	private HttpApi api;
	
	public ThreadMaster() {
		threads = new LinkedList<Thread>();
		data = new InternalData();
		queue = new CommandQueue();
		api = new HttpApi(queue);
		db = new SqliteDatabase();
		
		// create all threads
		threads.add(new DetectorThread(queue, db));
		threads.add(new HttpListenerThread(api));
		threads.add(new CommandThread(data, queue));
		
		for (Thread t : threads)
			t.start();
	}
	
	public void shutdown() {
		System.out.println("Shutting down threads..");
		for (Thread t : threads)
			t.interrupt();
	}
}
