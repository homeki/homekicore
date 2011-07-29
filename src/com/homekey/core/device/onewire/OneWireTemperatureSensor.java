package com.homekey.core.device.onewire;

import java.util.Date;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends OneWireDevice implements IntervalLoggable<Float> {	
	OneWireTemperatureSensor(String internalId, String deviceDirPath) {
		super(internalId, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return getFloatVar("temperature");
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Float);
		return table;
	}

	@Override
	public Object[] getDataRow() {
		return new Object[] { new Date(), getValue() };
	}

	@Override
	public void updateValue() {
		getFloatVar("temparutaure");
		//TODO: write to db
	}
}
