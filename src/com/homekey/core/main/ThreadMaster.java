package com.homekey.core.main;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.http.HttpListenerThread;

public class ThreadMaster {
	CommandsThread ct;
	Monitor monitor;
	HttpListenerThread httpThread;
	public ThreadMaster(){
		monitor = new Monitor();
		httpThread = new HttpListenerThread(monitor);
		
		
		ct = new CommandsThread(monitor);
		ct.start();
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public CommandsThread getCommandThread() {
		return ct;
	}

	public void shutdown() {
		System.out.println("Shutting down threads..");
		ct.interrupt();
		httpThread.interrupt();
	}
}
