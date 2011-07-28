package com.homekey.core.command;

import com.homekey.core.main.InternalData;

public class CommandsThread extends Thread {
	private CommandQueue queue;
	private InternalData data;
	
	public CommandsThread(InternalData data, CommandQueue queue) {
		this.data = data;
		this.queue = queue;
	}
	
	@Override
	public synchronized void run() {
		while (true) {
			Command<?> c = null;
			try {
				c = queue.takeCommand();
			} catch (InterruptedException e) {
				System.out.println("Killing CommandsThread.");
				return;
			}
			
			if (c != null) {
				System.out.println("Took command: " + c.toString());
				c.run(data);
			}
		}
	}	
}