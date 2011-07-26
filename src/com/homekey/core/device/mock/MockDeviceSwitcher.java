package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceSwitcher extends Device implements Switchable, Queryable {
	
	private boolean talk;
	private boolean on;
	
	public MockDeviceSwitcher(int id, String name, boolean talk) {
		super(id, name);
		this.on = false;
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceSwitcher called '" + getName() + "' with id=" + id + ".");
	}
	
	@Override
	public void off() {
		on = false;
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		on = true;
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now ON!");
	}

	@Override
	public String getStatus() {
		return String.format("The device is %s", on ? "on" : "off");
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "Registered", ColumnType.DateTime);
		table.setColumn(1, "Value", ColumnType.Boolean);
		return table;
	}
}
