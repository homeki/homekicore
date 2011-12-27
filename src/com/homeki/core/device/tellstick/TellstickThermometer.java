package com.homeki.core.device.tellstick;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HTemperatureHistoryPoint;

public class TellstickThermometer extends Device implements IntervalLoggable<Double> {
	public TellstickThermometer(String internalId) {
		super(internalId);
	}

	@Override
	public Double getValue() {
		return Hibernate.getLatestTemperatureHistoryPointValue(id);
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getTemperatureHistoryPoints(from, to);
	}

	@Override
	public String getType() {
		return "thermometer";
	}

	@Override
	public void setValue(Double value) {
		Hibernate.putHistoryValue(id, new HTemperatureHistoryPoint(value));
	}

	@Override
	public void updateValue() {
	}
}
