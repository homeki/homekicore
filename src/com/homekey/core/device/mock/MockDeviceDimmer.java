package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceDimmer extends Device implements Dimmable {
	
	private boolean talk;
	
	public MockDeviceDimmer(String internalId, String name, boolean talk) {
		super(internalId, name);
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceDimmer called '" + getName() + "'.");
	}
	
	@Override
	public void dim(int level) {
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level " + level + ".");
	}
	
	@Override
	public boolean off() {
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' is now OFF!");
		return true;
	}
	
	@Override
	public boolean on() {
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName()+ "' is now ON!");
		return true;
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "Registered", ColumnType.DateTime);
		table.setColumn(1, "Value", ColumnType.Integer);
		return table;
	}
}
