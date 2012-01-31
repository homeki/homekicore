package com.homeki.core.device.mock;

import java.util.Date;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.DimmerHistoryPoint;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.device.SwitchHistoryPoint;
import com.homeki.core.device.TemperatureHistoryPoint;
import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.Module;
import com.homeki.core.storage.Hibernate;

public class MockModule implements Module {
	@Override
	public void construct(ConfigurationFile file) {
		Session session = Hibernate.openSession();
		
		addMockDevice(session, "switch1", new MockSwitch(), new SwitchHistoryPoint(false));
		addMockDevice(session, "switch2", new MockSwitch(), new SwitchHistoryPoint(false));
		addMockDevice(session, "dimmer1", new MockDimmer(), new DimmerHistoryPoint(0));
		addMockDevice(session, "temp1", new MockThermometer(), new TemperatureHistoryPoint(0.0));
		addMockDevice(session, "temp2", new MockThermometer(), new TemperatureHistoryPoint(0.0));
		
		Hibernate.closeSession(session);
	}
	
	private void addMockDevice(Session session, String internalId, Device newdev, HistoryPoint hp) {
		Device dev = Device.getByInternalId(session, internalId);
		
		if (dev == null) {
			newdev.setInternalId(internalId);
			newdev.getHistoryPoints().add(hp);
			hp.setDevice(newdev);
			hp.setRegistered(new Date());
			session.save(newdev);
		}
	}
	
	@Override
	public void destruct() {
		
	}
}
