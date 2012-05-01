package com.homeki.core.events;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EventQueue {
	private static EventQueue instance;
	private BlockingQueue<Event> queue;
	
	private EventQueue() {
		this.queue = new LinkedBlockingQueue<Event>();
	}

	public static synchronized EventQueue getInstance() {
		if (instance == null)
			instance = new EventQueue();
		
		return instance;
	}
	
	public void add(Event e) {
		queue.add(e);
	}
	
	public Event take() throws InterruptedException {
		return queue.take();
	}
}
