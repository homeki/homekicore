package com.homekey.core.device.onewire;

import java.util.Date;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.IHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class OneWireTemperatureDevice extends OneWireDevice implements IntervalLoggable<Float> {
	private IHistoryTable historyTable;
	
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
		historyTable.putValue(new Date(), value);
	}

	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		historyTable = factory.getHistoryTable(tableName, Float.class);
	}
}
