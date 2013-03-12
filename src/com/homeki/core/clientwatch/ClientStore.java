package com.homeki.core.clientwatch;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum ClientStore {
	INSTANCE;
	
	class Client {
		InetAddress ip;
		int failCount;
		
		Client(InetAddress ip) {
			this.ip = ip;
		}
		
		@Override
		public int hashCode() {
			return ip.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return ip.equals(obj);
		}
	}
	
	private Set<Client> clients;
	
	private ClientStore() {
		this.clients = new HashSet<Client>();
	}
	
	public synchronized void addClient(InetAddress ip) {
		clients.add(new Client(ip));
	}
	
	public synchronized void removeClient(InetAddress ip) {
		clients.remove(ip);
	}
	
	public synchronized List<Client> getClients() {
		return new ArrayList<Client>(clients);
	}
}
