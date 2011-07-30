package com.homekey.core.device.mock;

import com.homekey.core.Logs;
import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.log.L;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class MockDimmerDevice extends Device implements Dimmable, Queryable<Integer> {
	private int level;
	
	public MockDimmerDevice(String internalId, Database db) {
		super(internalId, db);
		L.getLogger(Logs.MOCK).log("MockInfo: Created MockDeviceDimmer called '" + getName() + "'.");
	}
	
	@Override
	public void dim(int level) {
		this.level = level;
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
	public void createDatabaseTable() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DATETIME);
		table.setColumn(1, "value", ColumnType.INTEGER);
		db.createTable(databaseTableName, table);
	}
	
	@Override
	public Integer getValue() {
		return level;
	}
}
