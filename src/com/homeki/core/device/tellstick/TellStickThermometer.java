package com.homeki.core.device.tellstick;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.TemperatureHistoryPoint;

public class TellStickThermometer extends Device implements IntervalLoggable<Double> {
	@Override
	public Double getValue() {
		return null;
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getTemperatureHistoryPoints(from, to);
	}

	@Override
	public void updateValue() {
	}

	@Override
	public String getOuterType() {
		return "thermometer";
	}
}
