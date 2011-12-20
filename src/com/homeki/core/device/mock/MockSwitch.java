package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HSwitchHistoryPoint;

public class MockSwitch extends MockDevice implements Switchable, Queryable<Boolean> {
	public MockSwitch(String internalId) {
		super(internalId);
	}
	
	@Override
	public void off() {
		L.i("MockSwitchDevice '" + getInternalId() + "' is now OFF.");
		Hibernate.putHistoryValue(id, new HSwitchHistoryPoint(false));
	}
	
	@Override
	public void on() {
		L.i("MockSwitchDevice '" + getInternalId() + "' is now ON.");
		Hibernate.putHistoryValue(id, new HSwitchHistoryPoint(true));
	}
	
	@Override
	public Boolean getValue() {
		return Hibernate.getLatestSwitchHistoryPointValue(id);
	}
	
	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getSwitchHistoryPoints(from, to);
	}

	@Override
	public String getType() {
		return "switch";
	}
}
