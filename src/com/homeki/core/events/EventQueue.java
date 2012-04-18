package com.homeki.core.events;

import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
	private static EventQueue instance;
	
	private Queue<Event> queue;
	
	private EventQueue() {
		this.queue = new LinkedList<Event>();
	}

	public static synchronized EventQueue getInstance() {
		if (instance == null)
			instance = new EventQueue();
		
		return instance;
	}
	
	public synchronized void push(Event e) {
		queue.add(e);
		notifyAll();
	}
	
	public synchronized Event pop() throws InterruptedException {
		while (queue.isEmpty())
			wait();
		
		return queue.poll();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new CloneNotSupportedException();
	}
}
