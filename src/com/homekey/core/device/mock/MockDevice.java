package com.homekey.core.device.mock;

import com.homekey.core.device.Device;
import com.homekey.core.storage.Database;

public class MockDevice extends Device {
	public MockDevice(String internalId, Database db) {
		super(internalId, db);
	}

	@Override
	protected void createDatabaseTable() {
		// no need for a table when we won't have any specific data for the device
	}
}
