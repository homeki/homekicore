package com.homeki.core.device.onewire;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.device.IntervalLoggable;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.IHistoryTable;
import com.homeki.core.storage.ITableFactory;

public class OneWireThermometer extends OneWireDevice implements IntervalLoggable<Float> {
	private IHistoryTable historyTable;
	
	public OneWireThermometer(String internalId, ITableFactory factory, String deviceDirPath) {
		super(internalId, factory, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return getFloatVar("Thermometer");
	}

	@Override
	public void updateValue() {
		float value = getFloatVar("Thermometer");
		historyTable.putValue(new Date(), value);
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return historyTable.getValues(from, to);
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
