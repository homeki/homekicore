package com.homekey.core.command;

import com.homekey.core.main.Monitor;

public class CommandsThread extends Thread {
	
	private Monitor monitor;
	
	public CommandsThread(Monitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public synchronized void run() {
		while (true) {
			Runnable c = null;
			try {
				c = monitor.takeCommand();
			} catch (InterruptedException e) {
				System.out.println("Killing CommandsThread.");
				return;
			}
			
			if (c != null) {
				System.out.println("Took command: " + c.toString());
				c.run();
			}
		}
	}	
}