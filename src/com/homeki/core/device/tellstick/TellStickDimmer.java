package com.homeki.core.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;

public class TellStickDimmer extends Device implements Dimmable, Switchable {
	@Override
	public void dim(int level) {
		TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		//setValue(level);
	}

	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		//setValue(0);
	}
	
	@Override
	public void on() {
		dim(255);
	}

	@Override
	public String getType() {
		return "dimmer";
	}
}
