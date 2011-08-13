package com.homekey.core.device.onewire;

import java.util.Date;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.IFloatHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class OneWireTemperatureDevice extends OneWireDevice implements IntervalLoggable<Float> {
	private IFloatHistoryTable floatHistoryTable;
	
	public OneWireTemperatureDevice(String internalId, ITableFactory factory, String deviceDirPath) {
		super(internalId, factory, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return getFloatVar("temperature");
	}

	@Override
	public void updateValue() {
		float value = getFloatVar("temperature");
		floatHistoryTable.putValue(new Date(), value);
	}

	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		floatHistoryTable = factory.getFloatHistoryTable(tableName);
	}
}
