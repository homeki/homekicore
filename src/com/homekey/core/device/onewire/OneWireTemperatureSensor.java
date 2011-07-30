package com.homekey.core.device.onewire;

import java.util.Date;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class OneWireTemperatureSensor extends OneWireDevice implements IntervalLoggable<Float> {	
	public OneWireTemperatureSensor(String internalId, Database db, String deviceDirPath) {
		super(internalId, db, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return getFloatVar("temperature");
	}
	
	@Override
	public void createDatabaseTable() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Float);
		db.createTable(databaseTableName, table);
	}

	@Override
	public void updateValue() {
		Float value = getFloatVar("temperature");
		db.addRow(databaseTableName, new String[] { "registered", "value" }, new Object[] { new Date(), value });
	}
}
