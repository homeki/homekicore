package com.homeki.core.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.IntervalLoggable;

public class TellStickThermometer extends Device implements IntervalLoggable {
	@Override
	public void updateValue() {
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
