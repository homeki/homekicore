package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.SwitchHistoryPoint;
import com.homeki.core.device.abilities.Switchable;

@Entity
public class TellStickSwitch extends Device implements Switchable {
	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		addHistoryValue(false);
	}
	
	@Override
	public void on() {
		TellStickNative.turnOn(Integer.parseInt(getInternalId()));
		addHistoryValue(true);
	}

	@Override
	public String getType() {
		return "switch";
	}
	
	public void addHistoryValue(boolean value) {
		SwitchHistoryPoint shp = new SwitchHistoryPoint();
		shp.setDevice(this);
		shp.setRegistered(new Date());
		shp.setValue(value);
		historyPoints.add(shp);
	}
}
