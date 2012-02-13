package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.DimmerHistoryPoint;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;

@Entity
public class TellStickDimmer extends TellStickDevice implements Dimmable, Switchable, TellStickLearnable {
	public TellStickDimmer() {
		
	}
	
	public TellStickDimmer(int defaultLevel) {
		addHistoryPoint(defaultLevel);
	}
	
	public TellStickDimmer(int defaultLevel, int house, int unit) {
		this(defaultLevel);
		int result = TellStickNative.addDimmer(house, unit);
		
		if (result < 0)
			throw new RuntimeException("Failed to add new TellStick dimmer device, error returned from Homeki JNI library was " + result + ".");
		
		this.internalId = String.valueOf(result);
	}
	
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
	
	public void addHistoryPoint(int value) {
		DimmerHistoryPoint dhp = new DimmerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		historyPoints.add(dhp);
	}

	@Override
	public void learn() {
		TellStickNative.learn(Integer.valueOf(internalId));
	}
}
