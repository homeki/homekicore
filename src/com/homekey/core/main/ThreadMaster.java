package com.homekey.core.main;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.http.HttpListenerThread;

public class ThreadMaster {
	CommandsThread ct;
	Monitor monitor;
	public ThreadMaster(){
		monitor = new Monitor();
		new HttpListenerThread(monitor).start();
		
		ct = new CommandsThread(monitor);
		ct.start();
	}

	public Monitor getMonitor() {
		return monitor;
	}

	public CommandsThread getCommandThread() {
		return ct;
	}
}
