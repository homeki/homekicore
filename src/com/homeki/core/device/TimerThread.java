package com.homeki.core.device;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class TimerThread extends ControlledThread {
	// SortedSet<TimerTrigger> timers;
	
	public TimerThread(int interval) {
		super(interval);
		// timers = new TreeSet<TimerTrigger>();
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<TimerTrigger> list = session.createCriteria(TimerTrigger.class).list();
		
		Calendar c = Calendar.getInstance();
		
		int current_time = c.get(Calendar.HOUR) * 3600 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
		System.out.println("Checking timers, current time is" + current_time);
		for (TimerTrigger t : list) {
			if (Math.abs(current_time - t.getSecondsFromMidnight()) < 15)
				switch (t.getRepeatType()) {
				case 0:
					fire(t);
					break;
				case 1:
					int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
					if (t.getDays() >> weekday == 1) {
						fire(t);
					}
					break;
				case 2:
					int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					if (t.getDays() >> dayOfMonth == 1) {
						fire(t);
					}
					break;
				
				default:
					break;
				}
		}
		
		Hibernate.closeSession(session);
	}
	
	public void fire(TimerTrigger t) {
		t.trigger();
	}
}
