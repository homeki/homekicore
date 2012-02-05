package com.homeki.core.device.mock;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.SwitchHistoryPoint;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.main.L;

@Entity
public class MockSwitch extends Device implements Switchable {
	public MockSwitch() {
		
	}
	
	public MockSwitch(boolean defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
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
	
	public void addHistoryPoint(boolean value) {
		SwitchHistoryPoint dhp = new SwitchHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		historyPoints.add(dhp);
	}
	
	@Override
	public String getType() {
		return "switch";
	}
}
