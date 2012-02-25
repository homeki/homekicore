package com.homeki.core.device.onewire;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class OneWireCollectorThread extends ControlledThread {
	private Set<String> loggedSet;
	
	public OneWireCollectorThread() {
		super(Configuration.ONEWIRE_COLLECTOR_INTERVAL);
		loggedSet = new HashSet<String>();
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<OneWireIntervalLoggable> devices = session.createCriteria(OneWireIntervalLoggable.class)
			.add(Restrictions.eq("active", true))
			.list();
		
		for (OneWireIntervalLoggable d : devices) {
			String internalId = d.getInternalId();
			
			try {
				d.updateValue();
				
				if (loggedSet.remove(internalId))
					L.i("Update value for device with internal ID " + internalId + " succeeded again.");
			} catch (Exception e) {
				if (loggedSet.add(internalId))
					L.e("Could not update value for device with internal ID " + internalId + ". Log message throttled until next success.", e);
			}
		}
		
		Hibernate.closeSession(session);
	}
}
