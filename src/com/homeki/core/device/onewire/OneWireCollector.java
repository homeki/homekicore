package com.homeki.core.device.onewire;

import java.util.List;

import org.hibernate.Session;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.storage.Hibernate;

public class OneWireCollector extends ControlledThread {
	public OneWireCollector(int interval) {
		super(interval);
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<OneWireIntervalLoggable> devices = session.createCriteria(OneWireIntervalLoggable.class).list();
		
		for (OneWireIntervalLoggable d : devices) {
			d.updateValue();
			session.save(d);
		}
		
		Hibernate.closeSession(session);
	}
}
