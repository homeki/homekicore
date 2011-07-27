package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceSwitcher extends Device implements Switchable, Queryable {
	
	private boolean talk;
	private boolean on;
	
	public MockDeviceSwitcher(String internalId, String name, boolean talk) {
		super(internalId, name);
		this.on = false;
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceSwitcher called '" + getName() + "'.");
	}
	
	@Override
	public boolean off() {
		on = false;
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now OFF!");
		return true;
	}
	
	@Override
	public boolean on() {
		on = true;
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now ON!");
		return true;
	}

	@Override
	public String getValue() {
		return  on ? "on" : "off";
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Boolean);
		return table;
	}
}
