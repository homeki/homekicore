package com.homekey.core.device.mock;

import java.util.Date;

import com.homekey.core.Logs;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.log.L;
import com.homekey.core.storage.IIntegerHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class MockDimmerDevice extends MockDevice implements Dimmable, Queryable<Integer> {
	private IIntegerHistoryTable history;
	
	public MockDimmerDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
		L.getLogger(Logs.MOCK).log("MockInfo: Created MockDeviceDimmer called '" + getName() + "'.");
	}
	
	@Override
	public void dim(int level) {
		history.putValue(new Date(), level);
		L.getLogger(Logs.MOCK).log("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level " + level + ".");
	}
	
	@Override
	public void off() {
		dim(0);
		L.getLogger(Logs.MOCK).log("MockInfo: MockDeviceDimmer called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		dim(255);
		L.getLogger(Logs.MOCK).log("MockInfo: MockDeviceDimmer called '" + getName() + "' is now ON!");
	}
	
	@Override
	public Integer getValue() {
		return history.getLatestValue();
	}
	
	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		history = factory.getIntegerHistoryTable(tableName);
		history.ensureTable();
	}
}
