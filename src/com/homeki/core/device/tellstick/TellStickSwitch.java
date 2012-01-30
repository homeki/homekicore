package com.homeki.core.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Switchable;

public class TellStickSwitch extends Device implements Switchable {
	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		//setValue(false);
	}
	
	@Override
	public void on() {
		TellStickNative.turnOn(Integer.parseInt(getInternalId()));
		//setValue(true);
	}

	@Override
	public String getType() {
		return "switch";
	}
}
