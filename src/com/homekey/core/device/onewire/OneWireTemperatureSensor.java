package com.homekey.core.device.onewire;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends OneWireDevice implements IntervalLoggable<Float> {
	public OneWireTemperatureSensor(String internalId, String deviceDirPath) {
		super(internalId, deviceDirPath);
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Float getValue() {
		return null;
	}
}
