package com.homeki.core.device.onewire;

import com.homeki.core.device.abilities.IntervalLoggable;

public class OneWireThermometer extends OneWireDevice implements IntervalLoggable {
	public OneWireThermometer(String internalId, String deviceDirPath) {
		super(deviceDirPath);
	}

	@Override
	public void updateValue() {
		double value = getDoubleVar("Thermometer");
		//setValue(value);
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
