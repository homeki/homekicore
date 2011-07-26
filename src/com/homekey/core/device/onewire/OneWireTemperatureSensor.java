package com.homekey.core.device.onewire;

import com.homekey.core.device.Device;
import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends Device implements IntervalLoggable {
	public OneWireTemperatureSensor(int id, String name) {
		super(id, name);
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		throw new UnsupportedOperationException();
	}
}
