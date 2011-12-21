package com.homeki.core.device.tellstick;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HSwitchHistoryPoint;

public class TellStickSwitch extends Device implements Switchable, Queryable<Boolean> {
	public TellStickSwitch(String internalId) {
		super(internalId);
	}
	
	@Override
	public void off() {
		TellStickNative.turnOff(Integer.parseInt(getInternalId()));
		setValue(false);
	}
	
	@Override
	public void on() {
		TellStickNative.turnOn(Integer.parseInt(getInternalId()));
		setValue(true);
	}
	
	@Override
	public Boolean getValue() {
		return Hibernate.getLatestSwitchHistoryPointValue(id);
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getSwitchHistoryPoints(from, to);
	}

	@Override
	public String getType() {
		return "switch";
	}

	@Override
	public void setValue(Boolean value) {
		if (!value.equals(getValue())) {
			Hibernate.putHistoryValue(id, new HSwitchHistoryPoint(value));
		}
	}
}
