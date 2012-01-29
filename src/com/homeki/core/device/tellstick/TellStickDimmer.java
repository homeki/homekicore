package com.homeki.core.device.tellstick;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.DimmerHistoryPoint;

public class TellStickDimmer extends Device implements Dimmable, Switchable, Queryable<Integer> {
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
	public Integer getValue() {
		return -1;
	}
	
	@Override
	public void on() {
		dim(255);
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return null;
	}

	@Override
	public String getOuterType() {
		return "dimmer";
	}
}
