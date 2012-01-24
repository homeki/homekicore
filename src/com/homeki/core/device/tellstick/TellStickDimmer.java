package com.homeki.core.device.tellstick;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HDimmerHistoryPoint;

public class TellStickDimmer extends Device implements Dimmable, Switchable, Queryable<Integer> {
	public TellStickDimmer(String internalId) {
		super(internalId);
	}

	@Override
	public void dim(int level) {
		TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		setValue(level);
	}

	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		setValue(0);
	}

	@Override
	public Integer getValue() {
		return Hibernate.getLatestDimmerHistoryPointValue(id);
	}
	
	@Override
	public void on() {
		dim(255);
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getDimmerHistoryPoints(from, to);
	}

	@Override
	public String getType() {
		return "dimmer";
	}

	@Override
	public void setValue(Integer value) {
		if (!value.equals(getValue())) {
			Hibernate.putHistoryValue(id, new HDimmerHistoryPoint(value));
		}
	}
}
