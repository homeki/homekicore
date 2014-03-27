package com.homeki.core.clientwatch;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.logging.L;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum ClientStore {
	INSTANCE;
	
	private Set<String> clients;
	
	private ClientStore() {
		this.clients = new HashSet<>();
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

	public synchronized List<String> listClients() {
		return new ArrayList<>(clients);
	}
}
