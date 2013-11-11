package com.homeki.core.clientwatch;

import java.util.HashSet;
import java.util.Set;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.logging.L;

public enum ClientStore {
	INSTANCE;
	
	private Set<String> clients;
	
	private ClientStore() {
		this.clients = new HashSet<String>();
	}
	
	public synchronized void addClient(String id) {
		clients.add(id);
		EventQueue.INSTANCE.add(SpecialValueChangedEvent.createClientWatchEvent(clients.size()));
		L.i("New client " + id + " registered.");
	}
	
	public synchronized void removeClient(String id) {
		if (clients.remove(id)) {
			EventQueue.INSTANCE.add(SpecialValueChangedEvent.createClientWatchEvent(clients.size()));
			L.i("Client " + id + " was removed.");
		}
	}
}
