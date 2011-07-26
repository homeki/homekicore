package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.storage.DatabaseTable;

public class MockDeviceDimmer extends Device implements Dimmable {
	
	private boolean talk;
	
	public MockDeviceDimmer(int id, String name, boolean talk) {
		super(id, name);
		this.talk = talk;
		if (talk)
			System.out.println("MockInfo: Created MockDeviceDimmer called '" + getName() + "' with id=" + id);
	}
	
	@Override
	public void dim(int level) {
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level " + level + ".");
	}
	
	@Override
	public void off() {
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		if (talk)
			System.out.println("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level" + level + ".");
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		throw new UnsupportedOperationException();
	}
}
