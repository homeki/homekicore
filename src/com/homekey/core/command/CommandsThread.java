package com.homekey.core.command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandsThread extends Thread {
	BlockingQueue<Command<?>> workQueue = new LinkedBlockingQueue<Command<?>>();
	
	@Override
	public void run() {
		while (true) {
			Runnable c = null;
			try {
				c = workQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (c != null) {
				c.run();
			}
		}
	}
	
	public void post(Command<?> c) {		
		workQueue.offer(c);
	}
	
}