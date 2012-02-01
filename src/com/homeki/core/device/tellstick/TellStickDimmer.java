package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.DimmerHistoryPoint;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;

@Entity
public class TellStickDimmer extends Device implements Dimmable, Switchable {
	@Override
	public void dim(int level) {
		if (level > 0) {
			TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		} else if (level == 0) {
			off();
		}
	}

	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
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
