package com.homekey.core.command;

import com.homekey.core.main.InternalData;
import com.homekey.core.storage.Database;

public class CommandThread extends Thread {
	private CommandQueue queue;
	private InternalData data;
	
	public CommandThread(InternalData data, CommandQueue queue) {
		this.data = data;
		this.queue = queue;
	}
	
	@Override
	public void run() {
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