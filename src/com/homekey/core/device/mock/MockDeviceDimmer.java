package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceDimmer extends Device implements Dimmable, Queryable<Integer> {
	private boolean talk;
	private int level;
	
	public MockDeviceDimmer(String internalId, boolean talk) {
		super(internalId);
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceDimmer called '" + getName() + "'.");
	}
	
	@Override
	public void dim(int level) {
		dim(level);
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level " + level + ".");
	}
	
	@Override
	public void off() {
		dim(0);
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		dim(255);
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' is now ON!");
	}
	
	@Override
	public void createDatabaseTable() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Integer);
		db.createTable(databaseTableName, table);
	}
	
	@Override
	public Integer getValue() {
		return level;
	}
}
