package com.homeki.core.device.tellstick;

import java.util.Date;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.storage.entities.DimmerHistoryPoint;

public class TellStickDimmer extends Device implements Dimmable, Switchable {
	@Override
	public void dim(int level) {
		TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		addHistoryValue(level);
	}

	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		addHistoryValue(0);
	}
	
	@Override
	public void on() {
		dim(255);
	}

	@Override
	public String getType() {
		return "dimmer";
	}
	
	public void addHistoryValue(int value) {
		DimmerHistoryPoint dhp = new DimmerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		historyPoints.add(dhp);
	}
}
