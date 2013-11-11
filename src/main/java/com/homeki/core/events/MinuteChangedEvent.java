package com.homeki.core.events;


public class MinuteChangedEvent extends Event {
	public final int weekday;
	public final int day;
	public final int hour;
	public final int minute;
	
	public MinuteChangedEvent(int weekday, int day, int hour, int minute) {
		this.weekday = weekday;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}

}
