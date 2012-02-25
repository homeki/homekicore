package com.homeki.core.device;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class TimerThread extends ControlledThread {
	public TimerThread() {
		super(Configuration.TIMER_THREAD_INTERVAL);
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<TimerTrigger> list = session.createCriteria(TimerTrigger.class).list();
		
		Calendar c = Calendar.getInstance();
		int currentTime = c.get(Calendar.HOUR_OF_DAY) * 3600 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
		for (TimerTrigger t : list) {
			try {
				if (Math.abs(currentTime - t.getSecondsFromMidnight()) < 15)
					switch (t.getRepeatType()) {
					case 0:
						t.trigger();
						break;
					case 1:
						int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
						if (t.getDays() >> weekday == 1)
							t.trigger();
						break;
					case 2:
						int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
						if (t.getDays() >> dayOfMonth == 1)
							t.trigger();
						break;
					
					default:
						break;
					}
			} catch (Exception e) {
				L.e("Failed to trigger timer trigger with ID '" + t.getId() + "' and name '" + t.getName() + "'.", e);
			}
		}
		
		Hibernate.closeSession(session);
	}
}
