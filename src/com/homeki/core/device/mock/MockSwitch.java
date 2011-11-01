package com.homeki.core.device.mock;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.Logs;
import com.homeki.core.device.Queryable;
import com.homeki.core.device.Switchable;
import com.homeki.core.log.L;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.IHistoryTable;
import com.homeki.core.storage.ITableFactory;

public class MockSwitch extends MockDevice implements Switchable, Queryable<Boolean> {
	
	public MockSwitch(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}
	
	@Override
	public void off() {
		historyTable.putValue(new Date(), false);
		L.getLogger(Logs.CORE_MOCK).log("MockSwitchDevice '" + getInternalId() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		historyTable.putValue(new Date(), true);
		L.getLogger(Logs.CORE_MOCK).log("MockSwitchDevice '" + getInternalId() + "' is now ON!");
	}
	
	@Override
	public Boolean getValue() {
		return (Boolean)historyTable.getLatestValue();
	}
	
	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return historyTable.getValues(from, to);
	}

	@Override
	protected Type getTableValueType() {
		return  Boolean.class;
	}
}
