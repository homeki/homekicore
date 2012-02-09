package com.homeki.core.device.onewire;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class OneWireCollector extends ControlledThread {
	private Set<String> loggedSet;
	
	public OneWireCollector() {
		super(Configuration.ONEWIRE_COLLECTOR_INTERVAL);
		loggedSet = new HashSet<String>();
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<OneWireIntervalLoggable> devices = session.createCriteria(OneWireIntervalLoggable.class).list();
		
		for (OneWireIntervalLoggable d : devices) {
			String internalId = ((Device)d).getInternalId();
			
			try {
				d.updateValue();
				
				if (loggedSet.remove(internalId))
					L.i("Update value for device with internal id " + internalId + " succeeded again.");
			} catch (Exception ex) {
				if (loggedSet.add(internalId))
					L.e("Could not update value for device with internal id " + internalId + ". Log message throttled until next success.", ex);
			}
		}
		
		Hibernate.closeSession(session);
	}
}
