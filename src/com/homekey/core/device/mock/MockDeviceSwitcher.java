package com.homekey.core.device.mock;

import com.homekey.core.Logs;
import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.log.L;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceSwitcher extends Device implements Switchable, Queryable<Boolean> {
	private boolean talk;
	private boolean on;
	
	public MockDeviceSwitcher(String internalId, Database db) {
		super(internalId, db);
		L.getLogger(Logs.MOCK).log("MockInfo: Created MockDeviceSwitcher called '" + getName() + "'.");
	}
	
	@Override
	public void off() {
		on = false;
		L.getLogger(Logs.MOCK).log("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		on = true;
		L.getLogger(Logs.MOCK).log("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now ON!");
	}
	
	@Override
	public Boolean getValue() {
		return on;
	}
	
	@Override
	public void createDatabaseTable() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Boolean);
		db.createTable(databaseTableName, table);
	}
}
