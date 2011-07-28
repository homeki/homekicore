package com.homekey.core.command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandQueue {
	private BlockingQueue<Command<?>> workQueue = new LinkedBlockingQueue<Command<?>>();

	// Should not be synchronized, since PQ is thread-safe.
	public void post(Command<?> c) {
		workQueue.add(c);
	}
	
	// Should not be synchronized, since PQ is thread-safe.
	public Command<?> takeCommand() throws InterruptedException {
		return workQueue.take();
	}
}
