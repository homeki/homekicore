package com.homeki.core.events;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.triggers.Trigger;
import org.hibernate.Session;

import java.util.List;

public class EventHandlerThread extends ControlledThread {
	public EventHandlerThread() {
		super(0);
	}
	
	protected void iteration() throws Exception {
		Event e = EventQueue.INSTANCE.take(); // will block until event received

		List<EventListener> listeners = EventQueue.INSTANCE.copyEventListeners();
		for (EventListener l : listeners) {
			l.onEvent(e);
		}

		Session ses = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Trigger> list = ses.createCriteria(Trigger.class).list();

		for (Trigger t : list) {
			if (!t.update(e))
				continue;

			if (t.isFulfilled())
				t.execute(ses);
		}
		
		Hibernate.closeSession(ses);
	}
}
