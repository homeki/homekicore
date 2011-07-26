package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.device.Switchable;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceSwitcher extends Device implements Switchable {
	
	private boolean talk;
	
	public MockDeviceSwitcher(int id, String name, boolean talk) {
		super(id, name);
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceSwitcher called '" + getName() + "' with id=" + id + ".");
	}
	
	@Override
	public void off() {
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		if (talk)
			System.out.println("MockInfo: MockDeviceSwitcher called '" + getName() + "' is now ON!");
	}

	@Override
	public DatabaseTable getTableDesign() {
		return null;
	}
}
