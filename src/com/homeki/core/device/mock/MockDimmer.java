package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.DimmerHistoryPoint;

public class MockDimmer extends Device implements Switchable, Dimmable, Queryable<Integer> {
	@Override
	public void dim(int level) {
		L.i("MockHistoryDimmerDevice '" + getInternalId() + "' now has dim level " + level + ".");
		//Hibernate.putHistoryValue(getId(), new HDimmerHistoryPoint(level));
	}
	
	@Override
	public void off() {
		dim(0);
	}
	
	@Override
	public void on() {
		dim(255);
	}
	
	@Override
	public Integer getValue() {
		return -1;
	}
	
	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return null;
	}

	@Override
	public String getOuterType() {
		return "dimmer";
	}
}
