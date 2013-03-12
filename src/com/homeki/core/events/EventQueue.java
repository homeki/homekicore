package com.homeki.core.events;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum EventQueue {
	INSTANCE;
	
	private BlockingQueue<Event> queue;
	
	private EventQueue() {
		this.queue = new LinkedBlockingQueue<Event>();
	}
	
	public void add(Event e) {
		queue.add(e);
	}
	
	public Event take() throws InterruptedException {
		return queue.take();
	}
}
