package com.homeki.core.events;

import java.util.List;

import org.hibernate.Session;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.triggers.Trigger;

public class EventHandlerThread extends ControlledThread {
	public EventHandlerThread() {
		super(0);
	}
	
	protected void iteration() throws Exception {
		Event e = EventQueue.getInstance().take(); // will block until event received
		Session ses = Hibernate.openSession();

		@SuppressWarnings("unchecked")
		List<Trigger> list = ses.createCriteria(Trigger.class).list();
		for (Trigger t:list){
			if (t.check(e)){
				t.execute(ses);
			}
		}
		ses.close();
	}
}
