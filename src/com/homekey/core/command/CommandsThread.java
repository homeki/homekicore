package com.homekey.core.command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.homekey.core.main.Monitor;

public class CommandsThread extends Thread {
	
	private Monitor monitor;
	
	public CommandsThread(Monitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
		while (true) {
			Runnable c = null;
			c = monitor.takeCommand();
			
			if (c != null) {
				c.run();
			}
		}
	}
	
	public void post(Command<?> c) {
		monitor.post(c);
		// workQueue.offer(c);
	}
	
}