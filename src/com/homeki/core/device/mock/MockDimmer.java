package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HDimmerHistoryPoint;

public class MockDimmer extends MockDevice implements Dimmable, Queryable<Integer> {
	public MockDimmer(String internalId) {
		super(internalId);
	}

	@Override
	public void dim(int level) {
		L.i("MockHistoryDimmerDevice '" + getInternalId() + "' now has dim level " + level + ".");
		Hibernate.putHistoryValue(id, new HDimmerHistoryPoint(level));
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
		return Hibernate.getLatestDimmerHistoryPointValue(id);
	}
	
	@Override
	public void setValue(Integer value) {
		if (!value.equals(getValue())) {
			Hibernate.putHistoryValue(id, new HDimmerHistoryPoint(value));
		}
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getDimmerHistoryPoints(from, to);
	}

	@Override
	public String getType() {
		return "dimmer";
	}
}
