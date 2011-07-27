package com.homekey.core.device.onewire;

import com.homekey.core.device.Device;
import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends Device implements IntervalLoggable {
	public OneWireTemperatureSensor(String internalId, String name) {
		super(internalId, name);
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getValue() {
		return null;
	}
}
