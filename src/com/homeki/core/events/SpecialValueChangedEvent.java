package com.homeki.core.events;

public class SpecialValueChangedEvent extends Event {
	public final String source;
	public final int value;
	
	private SpecialValueChangedEvent(String source, int value) {
		super();
		this.source = source;
		this.value = value;
	}
	
	public static Event CreateClientWatchEvent(int numberOfClients) {
		return new SpecialValueChangedEvent("CONNECTED_CLIENTS", numberOfClients);
	}
}
