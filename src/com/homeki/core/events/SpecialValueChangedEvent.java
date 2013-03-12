package com.homeki.core.events;

import java.util.Arrays;
import java.util.List;

public class SpecialValueChangedEvent extends Event {
	private static final String CLIENT_WATCH = "CONNECTED_CLIENTS";
	
	public final String source;
	public final int value;
	
	private SpecialValueChangedEvent(String source, int value) {
		super();
		this.source = source;
		this.value = value;
	}
	
	public static Event createClientWatchEvent(int numberOfClients) {
		return new SpecialValueChangedEvent(CLIENT_WATCH, numberOfClients);
	}
	
	public static boolean verifySource(String source) {
		List<String> validSources = Arrays.asList(new String[] { 
				CLIENT_WATCH
		});
		
		return validSources.contains(source);
	}
}
