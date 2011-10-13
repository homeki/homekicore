package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;

import com.homeki.core.Logs;
import com.homeki.core.device.Queryable;
import com.homeki.core.device.Switchable;
import com.homeki.core.log.L;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.IHistoryTable;
import com.homeki.core.storage.ITableFactory;

public class MockSwitchDevice extends MockDevice implements Switchable, Queryable<Boolean> {
	private IHistoryTable historyTable;
	
	public MockSwitchDevice(String internalId, ITableFactory factory) {
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
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		historyTable = factory.getHistoryTable(tableName, Boolean.class);
		historyTable.ensureTable();
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return historyTable.getValues(from, to);
	}
}
