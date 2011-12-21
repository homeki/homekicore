package com.homeki.core.device.onewire;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HTemperatureHistoryPoint;

public class OneWireThermometer extends OneWireDevice implements IntervalLoggable<Float> {
	public OneWireThermometer(String internalId, String deviceDirPath) {
		super(internalId, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return getFloatVar("Thermometer");
	}

	@Override
	public void updateValue() {
		float value = getFloatVar("Thermometer");
		setValue(value);
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
	public void setValue(Float value) {
		Hibernate.putHistoryValue(id, new HTemperatureHistoryPoint(value));
	}
}
