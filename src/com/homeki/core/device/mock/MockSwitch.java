package com.homeki.core.device.mock;

import java.util.Date;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.main.L;
import com.homeki.core.storage.entities.SwitchHistoryPoint;

public class MockSwitch extends Device implements Switchable {
	@Override
	public void off() {
		L.i("MockSwitchDevice '" + getInternalId() + "' is now OFF.");
		addHistoryPoint(false);
	}
	
	@Override
	public void on() {
		L.i("MockSwitchDevice '" + getInternalId() + "' is now ON.");
		addHistoryPoint(true);
	}
	
	private void addHistoryPoint(boolean value) {
		SwitchHistoryPoint shp = new SwitchHistoryPoint();
		shp.setDevice(this);
		shp.setRegistered(new Date());
		shp.setValue(value);
		historyPoints.add(shp);
	}
	
	@Override
	public String getType() {
		return "switch";
	}
}
