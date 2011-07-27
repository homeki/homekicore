package com.homekey.core.command;

import com.homekey.core.main.Monitor;

public class CommandsThread extends Thread {
	
	private Monitor monitor;
	
	public CommandsThread(Monitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
		boolean running = true;
		while (running) {
			Runnable c = null;
			try {
				c = monitor.takeCommand();
			} catch (InterruptedException e) {
				System.out.println("Killing CommandsThread.");
				running = false;
			}
			
			if (c != null) {
				System.out.println("Took command: " + c.toString());
				c.run();
			}
		}
	}
	
	// public void post(Command<?> c) {
	// throw new RuntimeException("Method is deprecated.");
	// // monitor.post(c);
	// // workQueue.offer(c);
	// }
	
}