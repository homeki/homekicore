package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.abilities.Settable;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class TellStickSwitch extends TellStickDevice implements Settable, TellStickLearnable {
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
		if (channel != TELLSTICKSWITCH_ONOFF_CHANNEL)
			throw new RuntimeException("Tried to set invalid channel " + channel + " on TellStickSwitch '" + getInternalId() + "'.");
		
		boolean on = value > 0;
		int internalId = Integer.parseInt(getInternalId());
		
		if (on)
			TellStickNative.turnOn(internalId);
		else
			TellStickNative.turnOff(internalId);
	}

	@Override
	public String getType() {
		return "switch";
	}
	
	public void addOnOffHistoryPoint(boolean on) {
		int value = on ? 1 : 0;
		IntegerHistoryPoint shp = new IntegerHistoryPoint();
		shp.setDevice(this);
		shp.setRegistered(new Date());
		shp.setValue(on ? 1 : 0);
		historyPoints.add(shp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), TELLSTICKSWITCH_ONOFF_CHANNEL, value));
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
