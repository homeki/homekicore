package com.homekey.core.device.mock;

import java.util.Date;

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
		setValue(0);
		if (talk)
			System.out.println("MockInfo: Created MockDeviceDimmer called '" + getName() + "'.");
	}
	
	@Override
	public void dim(int level) {
		setValue(level);
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level " + level + ".");
	}
	
	@Override
	public boolean off() {
		setValue(0);
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' is now OFF!");
		return true;
	}
	
	@Override
	public boolean on() {
		setValue(255);
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' is now ON!");
		return true;
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Integer);
		return table;
	}
	
	@Override
	public Object[] getDataRow() {
		return new Object[] { new Date(), getValue() };
	}
	
	@Override
	public Integer getValue() {
		return level;
	}

	@Override
	public void setValue(Integer value) {
		level = value;
	}
}
