package com.homeki.core.triggers;

import org.hibernate.Session;

import com.homeki.core.storage.Hibernate;

public class Timer {
	protected final int id;
	
	public Timer(int id) {
		Session session = Hibernate.openSession();
		HSwitchTimer timer = (HSwitchTimer) session.createQuery("from HSwitchTimer as t where t.id = ?").setInteger(0, id).uniqueResult();
		
		if (timer == null) {
			timer = new HSwitchTimer();
			timer.setName("");
			timer.setDescription("");
			timer.setStart(0);
			timer.setStop(0);
			timer.setDays(0);
			this.id = (Integer) session.save(timer);
		} else {
			this.id = timer.getId();
		}
		
		Hibernate.closeSession(session);
	}
	
	public void setRepeatType(int rType) {
//		Session session = Hibernate.openSession();
//		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
//		hst.setDescription(description);
//		Hibernate.closeSession(session);
	}
	
	public int getRepeatType() {
		return 0;
//		Session session = Hibernate.openSession();
//		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
//		hst.setDescription(description);
//		Hibernate.closeSession(session);
	}
	
	public void setTime(int time) {
		Session session = Hibernate.openSession();
		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
		hst.setStart(time);
		Hibernate.closeSession(session);
	}
	
	public int getTime() {
		Session session = Hibernate.openSession();
		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
		int start = hst.getStart();
		Hibernate.closeSession(session);
		return start;
	}
	
	public void setDays(int days) {
		Session session = Hibernate.openSession();
		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
		hst.setDays(days);
		Hibernate.closeSession(session);
	}
	
	public int getDays() {
		Session session = Hibernate.openSession();
		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
		int days = hst.getDays();
		Hibernate.closeSession(session);
		return days;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Timer) obj).id;
	}
	
}
