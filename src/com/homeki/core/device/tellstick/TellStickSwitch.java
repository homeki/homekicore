package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.abilities.Settable;
import com.homeki.core.device.abilities.Triggable;
import com.homeki.core.main.L;

@Entity
public class TellStickSwitch extends TellStickDevice implements Settable, Triggable, TellStickLearnable {
	private static final int TELLSTICKSWITCH_ONOFF_CHANNEL = 0;
	
	public TellStickSwitch() {
		
	}
	
	public TellStickSwitch(boolean defaultValue) {
		addOnOffHistoryPoint(defaultValue);
	}
	
	public TellStickSwitch(boolean defaultValue, int house, int unit) {
		this(defaultValue);
		
		int result = TellStickNative.addSwitch(house, unit);
		
		this.internalId = String.valueOf(result);
	}
	
	@Override
	public void set(int channel, int value) {
		if (channel == TELLSTICKSWITCH_ONOFF_CHANNEL)
			throw new RuntimeException("Tried to set invalid channel " + channel + " on TellStickSwitch '" + getInternalId() + "'.");
	}

	@Override
	public void trigger(int newValue) {
		L.i("TellStickDimmer with internal ID'" + getInternalId() + "' triggered with newValue " + newValue + ".");
		set(TELLSTICKSWITCH_ONOFF_CHANNEL, newValue);
	}

	@Override
	public String getType() {
		return "switch";
	}
	
	public void addOnOffHistoryPoint(boolean on) {
		IntegerHistoryPoint shp = new IntegerHistoryPoint();
		shp.setDevice(this);
		shp.setRegistered(new Date());
		shp.setValue(on ? 1 : 0);
		historyPoints.add(shp);
	}

	@Override
	public void learn() {
		TellStickNative.learn(Integer.valueOf(internalId));
	}
	
	@Override
	public void preDelete() {
		TellStickNative.removeDevice(Integer.valueOf(internalId));
	}
}
