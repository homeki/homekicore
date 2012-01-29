package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.SwitchHistoryPoint;

public class MockSwitch extends Device implements Switchable, Queryable<Boolean> {
	@Override
	public void off() {
		L.i("MockSwitchDevice '" + getInternalId() + "' is now OFF.");
		//setValue(false);
	}
	
	@Override
	public void on() {
		L.i("MockSwitchDevice '" + getInternalId() + "' is now ON.");
		//setValue(true);
	}
	
	@Override
	public Boolean getValue() {
		return false;
	}
	
	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return null;
	}

	@Override
	public String getOuterType() {
		return "switch";
	}
}
