package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.Session;

import com.homeki.core.device.DimmerHistoryPoint;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;

@Entity
public class TellStickDimmer extends TellStickDevice implements Dimmable, Switchable, TellStickLearnable {
	public TellStickDimmer() {
		
	}
	
	public TellStickDimmer(int defaultLevel) {
		int off = 0;
		addHistoryPoint(off, defaultLevel);
	}
	
	public TellStickDimmer(int defaultLevel, int house, int unit) {
		this(defaultLevel);
		
		int result = TellStickNative.addDimmer(house, unit);
		
		this.internalId = String.valueOf(result);
	}
	
	@Override
	public void dim(int level, boolean on) {
		if (on) {
			TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		} else {
			off();
		}
	}

	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
	}
	
	@Override
	public void on() {
		TellStickNative.turnOn(Integer.parseInt(getInternalId()));
	}

	@Override
	public String getType() {
		return "dimmer";
	}
	
	public void addHistoryPoint(int value, Session session) {
		DimmerHistoryPoint dhp = new DimmerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		int level = ((DimmerHistoryPoint)getState(session)).getLevel();
		dhp.setLevel(level);
		historyPoints.add(dhp);
	}
	
	public void addHistoryPoint(int value, int level) {
		DimmerHistoryPoint dhp = new DimmerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		dhp.setLevel(level);
		historyPoints.add(dhp);
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
