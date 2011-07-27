package com.homekey.core.device.tellstick;

import java.io.IOException;

import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class TellStickSwitch extends Device implements Switchable, Queryable {
	public final String TYPE = "SWITCH";
	private boolean talk;
	private boolean on;
	
	public TellStickSwitch(String internalId, String name, boolean talk) {
		super(internalId, name);
		this.on = false;
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceSwitcher called '" + getName() + "' with id=" + id + ".");
	}
	
	@Override
	public boolean off() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -f %s", internalId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		on = false;
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now OFF!");
		return true;
	}
	
	@Override
	public boolean on() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -n %s", internalId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		on = true;
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now ON!");
		return true;
	}

	@Override
	public String getValue() {
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
