package com.homekey.core.device.mock;

import com.homekey.core.Logs;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.log.L;
import com.homekey.core.storage.ITableFactory;


public class MockDimmerDevice extends MockDevice implements Dimmable, Queryable<Integer> {
	private int level;
	
	public MockDimmerDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
		L.getLogger(Logs.CORE_MOCK).log("MockInfo: Created MockDeviceDimmer called '" + getName() + "'.");
	}

	
	@Override
	public void dim(int level) {
		this.level = level;
		L.getLogger(Logs.CORE_MOCK).log("MockInfo: MockDeviceDimmer called '" + getName() + "' now has dim level " + level + ".");
	}
	
	@Override
	public void off() {
		dim(0);
		L.getLogger(Logs.CORE_MOCK).log("MockInfo: MockDeviceDimmer called '" + getName() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		dim(255);
		L.getLogger(Logs.CORE_MOCK).log("MockInfo: MockDeviceDimmer called '" + getName() + "' is now ON!");
	}
	
	@Override
	public Integer getValue() {
		return level;
	}
}
