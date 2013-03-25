package com.homeki.core.clientwatch;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.logging.L;

public enum ClientStore {
	INSTANCE;
	
	class Client {
		InetAddress ip;
		int failCount;
		
		Client(InetAddress ip) {
			this.ip = ip;
		}
	}
	
	private Map<String, Client> clients;
	
	private ClientStore() {
		this.clients = new HashMap<String, Client>();
	}
	
	public synchronized void addClient(InetAddress ip) {
		clients.put(ip.getHostAddress(), new Client(ip));
		EventQueue.INSTANCE.add(SpecialValueChangedEvent.createClientWatchEvent(clients.size()));
		L.i("New client " + ip.getHostAddress() + " registered.");
	}
	
	public synchronized void removeClient(InetAddress ip) {
		clients.remove(ip.getHostAddress());
		EventQueue.INSTANCE.add(SpecialValueChangedEvent.createClientWatchEvent(clients.size()));
		L.i("Client " + ip.getHostAddress() + " was removed.");
	}
	
	public synchronized List<Client> getClients() {
		return new ArrayList<Client>(clients.values());
	}
}
