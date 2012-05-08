package com.homeki.core.events;

import java.util.Calendar;
import java.util.Date;

public class MinuteChangedEvent extends Event {
	private static final Calendar calendar = Calendar.getInstance();
	
	public final int weekday;
	public final int day;
	public final int hour;
	public final int minute;
	
	public MinuteChangedEvent() {
		calendar.setTime(new Date());
		
		int tmpweekday = calendar.get(Calendar.DAY_OF_WEEK);
		
		switch (tmpweekday) {
		case Calendar.MONDAY:
			weekday = 1;
			break;
		case Calendar.TUESDAY:
			weekday = 2;
			break;
		case Calendar.WEDNESDAY:
			weekday = 3;
			break;
		case Calendar.THURSDAY:
			weekday = 4;
			break;
		case Calendar.FRIDAY:
			weekday = 5;
			break;
		case Calendar.SATURDAY:
			weekday = 6;
			break;
		case Calendar.SUNDAY:
			weekday = 7;
			break;
		default:
			weekday = -1;
		}
		
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
	}
}
