package com.homeki.core.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum EventQueue {
	INSTANCE;
	
	private BlockingQueue<Event> queue;
	private List<EventListener> listeners;
	
	private EventQueue() {
		this.queue = new LinkedBlockingQueue<>();
		this.listeners = new ArrayList<>();
	}
	
	public void add(Event e) {
		queue.add(e);
	}

	public synchronized List<EventListener> copyEventListeners() {
		return new ArrayList<>(listeners);
	}

	public synchronized void subscribe(EventListener listener) {
		listeners.add(listener);
	}

	public synchronized void unsubscribe(EventListener listener) {
		listeners.remove(listener);
	}
	
	public Event take() throws InterruptedException {
		return queue.take();
	}
}
