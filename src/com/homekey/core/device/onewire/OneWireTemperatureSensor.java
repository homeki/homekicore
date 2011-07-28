package com.homekey.core.device.onewire;

import java.util.Date;

import com.homekey.core.device.Queryable;
import com.homekey.core.device.Renewable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends OneWireDevice implements Renewable<Float>, Queryable<Float> {
	private Float temperature;
	
	OneWireTemperatureSensor(String internalId, String deviceDirPath) {
		super(internalId, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return temperature;
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
	public void setValue(Float value) {
		temperature = value;
	}

	@Override
	public Float getNewValue() {
		return getFloatVar("temperature");
	}
}
