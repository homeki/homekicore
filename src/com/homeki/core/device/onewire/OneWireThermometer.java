package com.homeki.core.device.onewire;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.DatumPoint;

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
		//historyTable.putValue(new Date(), value);
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return null;
		//return historyTable.getValues(from, to);
	}

	@Override
	protected Type getTableValueType() {
		return Float.class;
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
