package com.homekey.core.device.mock;

import java.util.Date;
import java.util.List;

import com.homekey.core.Logs;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.log.L;
import com.homekey.core.storage.DatumPoint;
import com.homekey.core.storage.IHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class MockDimmerDevice extends MockDevice implements Dimmable, Queryable<Integer> {
	private IHistoryTable historyTable;
	
	public MockDimmerDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
		L.getLogger(Logs.CORE_MOCK).log("Created MockHistoryDimmerDevice.");
	}

	@Override
	public void dim(int level) {
		historyTable.putValue(new Date(), level);
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
		return (Integer)historyTable.getLatestValue();
	}
	
	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		historyTable = factory.getHistoryTable(tableName, Integer.class);
		historyTable.ensureTable();
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return historyTable.getValues(from, to);
	}
}
