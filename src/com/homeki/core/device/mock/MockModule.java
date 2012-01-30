package com.homeki.core.device.mock;

import java.util.Date;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.Module;
import com.homeki.core.main.Monitor;
import com.homeki.core.storage.Hibernate;

public class MockModule implements Module {
	@Override
	public void construct(Monitor monitor, ConfigurationFile file) {
		Session session = Hibernate.openSession();
		
		addMockDevice(session, "switch1", new MockSwitch());
		addMockDevice(session, "switch2", new MockSwitch());
		addMockDevice(session, "dimmer1", new MockDimmer());
		addMockDevice(session, "temp1", new MockThermometer());
		addMockDevice(session, "temp2", new MockThermometer());
		
		Hibernate.closeSession(session);
	}
	
	private void addMockDevice(Session session, String internalId, Device newdev) {
		Device dev = Device.getByInternalId(session, internalId);
		
		if (dev == null) {
			newdev.setInternalId(internalId);
			newdev.setAdded(new Date());
			session.save(newdev);
		}
	}
	
	@Override
	public void destruct() {
		
	}
}
