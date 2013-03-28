package com.homeki.core.events;

import java.util.Arrays;
import java.util.List;

public class SpecialValueChangedEvent extends Event {
	private static final String CLIENT_WATCH = "CONNECTED_CLIENTS";
	private static final String SUNRISE_SUNSET = "SUNRISE_SUNSET";
	
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
	
	public static Event createSunriseEvent() {
		return new SpecialValueChangedEvent(SUNRISE_SUNSET, 1);
	}
	
	public static Event createSunsetEvent() {
		return new SpecialValueChangedEvent(SUNRISE_SUNSET, 0);
	}
	
	public static boolean verifySource(String source) {
		List<String> validSources = Arrays.asList(new String[] { 
				CLIENT_WATCH,
				SUNRISE_SUNSET
		});
		
		return validSources.contains(source);
	}
}
