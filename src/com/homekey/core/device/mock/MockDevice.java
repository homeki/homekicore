package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.storage.ITableFactory;

public class MockDevice extends Device {
	public MockDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}

	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		// no history for a simple mockdevice
	}
}
