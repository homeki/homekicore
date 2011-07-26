package com.homekey.core.device.onewire;

import com.homekey.core.device.Device;
import com.homekey.core.device.IntervalLoggable;

public class OneWireTemperatureSensor extends Device implements IntervalLoggable {
	public OneWireTemperatureSensor(int id, String name) {
		super(id, name);
	}
}
