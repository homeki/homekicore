package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.SwitchHistoryPoint;
import com.homeki.core.device.abilities.Switchable;

@Entity
public class TellStickSwitch extends TellStickDevice implements Switchable, TellStickLearnable {
	public TellStickSwitch() {
		
	}
	
	public TellStickSwitch(boolean defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
	public TellStickSwitch(boolean defaultValue, int house, int unit) {
		this(defaultValue);
		int result = TellStickNative.addSwitch(house, unit);
		
		if (result < 0)
			throw new RuntimeException("Failed to add new TellStick switch device, error returned from Homeki JNI library was " + result + ".");
		
		this.internalId = String.valueOf(result);
	}
	
	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		addHistoryPoint(false);
	}
	
	@Override
	public void on() {
		TellStickNative.turnOn(Integer.parseInt(getInternalId()));
		addHistoryPoint(true);
	}

	@Override
	public String getType() {
		return "switch";
	}
	
	public void addHistoryPoint(boolean value) {
		SwitchHistoryPoint shp = new SwitchHistoryPoint();
		shp.setDevice(this);
		shp.setRegistered(new Date());
		shp.setValue(value);
		historyPoints.add(shp);
	}

	@Override
	public void learn() {
		TellStickNative.learn(Integer.valueOf(internalId));
	}
}
