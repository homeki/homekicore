package com.homekey.core.device.mock;

import com.homekey.core.Logs;
import com.homekey.core.device.Device;
import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.log.L;
import com.homekey.core.storage.ITableFactory;

public class MockSwitchDevice extends MockDevice implements Switchable, Queryable<Boolean> {
	private boolean on;
	
	public MockSwitchDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
		L.getLogger(Logs.CORE_MOCK).log("Created MockSwitchDevice.");
	}
	
	@Override
	public void off() {
		on = false;
		L.getLogger(Logs.CORE_MOCK).log("MockSwitchDevice '" + getInternalId() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		on = true;
		L.getLogger(Logs.CORE_MOCK).log("MockSwitchDevice '" + getInternalId() + "' is now ON!");
	}
	
	@Override
	public Boolean getValue() {
		return on;
	}
}
