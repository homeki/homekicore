package com.homeki.core.triggers;

import org.hibernate.Session;

import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.HDevice;
import com.homeki.core.storage.entities.HSwitchTimer;

public class Trigger {
	protected final int id;
	
	public Trigger(int id) {
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
	
	public void setName(String name) {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice) session.load(HDevice.class, id);
		dev.setName(name);
		Hibernate.closeSession(session);
	}
	
	public String getName() {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice) session.load(HDevice.class, id);
		String name = dev.getName();
		Hibernate.closeSession(session);
		return name;
	}

	public void setValue(int value) {
//		Session session = Hibernate.openSession();
//		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
//		hst.setDescription(description);
//		Hibernate.closeSession(session);
	}
	
	public int getValue() {
		return 0;
//		Session session = Hibernate.openSession();
//		HSwitchTimer hst = (HSwitchTimer) session.load(HSwitchTimer.class, id);
//		hst.setDescription(description);
//		Hibernate.closeSession(session);
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
		return id == ((Trigger) obj).id;
	}
	
}
