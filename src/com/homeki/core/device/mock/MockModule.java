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
		
		SwitchHistoryPoint switch1hp = new SwitchHistoryPoint();
		switch1hp.setValue(false);
		
		SwitchHistoryPoint switch2hp = new SwitchHistoryPoint();
		switch2hp.setValue(false);
		
		DimmerHistoryPoint dimmer1hp = new DimmerHistoryPoint();
		dimmer1hp.setValue(0);
		
		TemperatureHistoryPoint temp1hp = new TemperatureHistoryPoint();
		temp1hp.setValue(0.0);
		
		TemperatureHistoryPoint temp2hp = new TemperatureHistoryPoint();
		temp2hp.setValue(0.0);
		
		addMockDevice(session, "switch1", new MockSwitch(), switch1hp);
		addMockDevice(session, "switch2", new MockSwitch(), switch2hp);
		addMockDevice(session, "dimmer1", new MockDimmer(), dimmer1hp);
		addMockDevice(session, "temp1", new MockThermometer(), temp1hp);
		addMockDevice(session, "temp2", new MockThermometer(), temp2hp);
		
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
