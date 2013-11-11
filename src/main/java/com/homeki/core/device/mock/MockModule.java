package com.homeki.core.device.mock;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.Module;
import com.homeki.core.storage.Hibernate;

public class MockModule implements Module {
	private static int count = 87;
	
	@Override
	public void construct() {
		if (Configuration.MOCK_ENABLED) {
			L.i("Mock module enabled.");
			Session session = Hibernate.openSession();
			
			addMockDevice(session, "switch1", new MockSwitch(false));
			addMockDevice(session, "switch2", new MockSwitch(false));
			addMockDevice(session, "dimmer1", new MockDimmer(0));
			addMockDevice(session, "temp1", new MockThermometer(0.0));
			addMockDevice(session, "temp2", new MockThermometer(0.0));
			
			Hibernate.closeSession(session);
		}
	}
	
	private void addMockDevice(Session session, String internalId, Device newdev) {
		Device dev = Device.getByInternalId(internalId);
		
		if (dev == null) {
			newdev.setInternalId(internalId);
			session.save(newdev);
		}
	}
	
	@Override
	public void destruct() {
		
	}
	
	public static int getNextCount() {
		return ++count;
	}
}
