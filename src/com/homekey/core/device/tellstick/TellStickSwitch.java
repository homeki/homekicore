package com.homekey.core.device.tellstick;

import java.io.IOException;
import java.util.Date;

import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class TellStickSwitch extends Device implements Switchable, Queryable<Boolean> {
	public TellStickSwitch(String internalId) {
		super(internalId);
	}
	
	@Override
	public void off() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -f %s", getInternalId()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		//TODO: log switch in db
	}
	
	@Override
	public void on() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -n %s", getInternalId()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		//TODO: log switch in db
	}
	
	@Override
	public Boolean getValue() {
		return null;
	}
	
	@Override
	public void createDatabaseTable() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "registered", ColumnType.DateTime);
		table.setColumn(1, "value", ColumnType.Boolean);
		db.createTable(databaseTableName, table);
	}
}
