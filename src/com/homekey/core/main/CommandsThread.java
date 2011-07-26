package com.homekey.core.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandsThread extends Thread {
	BlockingQueue<Command<?>> workQueue;
	
	@Override
	public void run() {
		workQueue = new LinkedBlockingQueue<Command<?>>();
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