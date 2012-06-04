package com.homeki.core.generators;

import java.util.Calendar;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.MinuteChangedEvent;
import com.homeki.core.main.ControlledThread;

public class ClockGeneratorThread extends ControlledThread {
	private static final Calendar calendar = Calendar.getInstance();
	
	public ClockGeneratorThread() {
		super(60*1000);
	}

	@Override
	protected void iteration() throws Exception {
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		int tempWeekday = calendar.get(Calendar.DAY_OF_WEEK);
		//week begins with sunday in java, but we want monday to be day 1!
		tempWeekday -= 1;
		if (tempWeekday == 0){
			tempWeekday = 7;
		}
		
		int weekday = tempWeekday;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		EventQueue.getInstance().add(new MinuteChangedEvent(weekday, day, hour, minute));
	}
}
