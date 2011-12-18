package com.homeki.core.device.mock;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.Logs;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.log.L;
import com.homeki.core.storage.DatumPoint;

public class MockDimmer extends MockDevice implements Dimmable, Queryable<Integer> {
	public MockDimmer(String internalId) {
		super(internalId);
		L.getLogger(Logs.CORE_MOCK).log("Created MockHistoryDimmerDevice.");
	}

	@Override
	public void dim(int level) {
		//historyTable.putValue(new Date(), level);
		L.getLogger(Logs.CORE_MOCK).log("MockHistoryDimmerDevice '" + getInternalId() + "' now has dim level " + level + ".");
	}
	
	@Override
	public void off() {
		dim(0);
		L.getLogger(Logs.CORE_MOCK).log("MockHistoryDimmerDevice '" + getInternalId() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		dim(255);
		L.getLogger(Logs.CORE_MOCK).log("MockHistoryDimmerDevice '" + getInternalId() + "' is now ON!");
	}
	
	@Override
	public Integer getValue() {
		//Object k = historyTable.getLatestValue();
		Object k = 0;
		return (Integer)k;
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		//return historyTable.getValues(from, to);
		return null;
	}

	@Override
	protected Type getTableValueType() {
		return Integer.class;
	}

	@Override
	public String getType() {
		return "dimmer";
	}
}
