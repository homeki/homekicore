package com.homeki.core.generators;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.MinuteChangedEvent;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Setting;
import com.homeki.core.storage.Hibernate;
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import org.hibernate.Session;

import java.util.Calendar;

public class ClockGeneratorThread extends ControlledThread {
	private final Calendar now = Calendar.getInstance();
	private Calendar sunrise;
	private Calendar sunset;
	
	public ClockGeneratorThread() {
		super(60*1000);
	}

	@Override
	protected void iteration() throws Exception {
		now.setTimeInMillis(System.currentTimeMillis());
		
		int tempWeekday = now.get(Calendar.DAY_OF_WEEK);
		
		// week begins with sunday in java, but we want monday to be day 1!
		tempWeekday -= 1;
		if (tempWeekday == 0)
			tempWeekday = 7;
		
		int weekday = tempWeekday;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		EventQueue.INSTANCE.add(new MinuteChangedEvent(weekday, day, hour, minute));
		
		if (hour == 2 && minute == 0 || sunrise == null || sunset == null)
			updateSunriseSunsetTimes();
		
		int sunriseHour = sunrise.get(Calendar.HOUR_OF_DAY);
		int sunriseMinute = sunrise.get(Calendar.MINUTE);
		int sunsetHour = sunset.get(Calendar.HOUR_OF_DAY);
		int sunsetMinute = sunset.get(Calendar.MINUTE);
		
		if (hour == sunriseHour && minute == sunriseMinute)
			EventQueue.INSTANCE.add(SpecialValueChangedEvent.createSunriseEvent());
		else if (hour == sunsetHour && minute == sunsetMinute)
			EventQueue.INSTANCE.add(SpecialValueChangedEvent.createSunsetEvent());
	}
	
	private void updateSunriseSunsetTimes() {
		Session ses = Hibernate.openSession();
		double la = Setting.getDouble(ses, Setting.LOCATION_LATITUDE);
		double lo = Setting.getDouble(ses, Setting.LOCATION_LONGITUDE);
		Hibernate.closeSession(ses);
		Location location = new Location(la, lo);
		SunriseSunsetCalculator calc = new SunriseSunsetCalculator(location, now.getTimeZone().getID());
		sunrise = calc.getCivilSunriseCalendarForDate(now);
		sunset = calc.getCivilSunsetCalendarForDate(now);
		
		// add constants so events are sent when its completely bright outside and when
		// it is starting to get dark
		sunrise.add(Calendar.MINUTE, Configuration.SUNSET_SUNRISE_OFFSET_MINUTES);
		sunset.add(Calendar.MINUTE, -Configuration.SUNSET_SUNRISE_OFFSET_MINUTES);
	}
}
