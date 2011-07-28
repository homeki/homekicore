package com.homekey.core.device.onewire;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends OneWireDevice implements IntervalLoggable<Float> {
	OneWireTemperatureSensor(String internalId, String deviceDirPath) {
		super(internalId, deviceDirPath);
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Float);
		return table;
	}

	@Override
	public Float getValue() {
		return getFloatVar("temperature");
	}
}
