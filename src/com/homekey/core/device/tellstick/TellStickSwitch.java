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
		//TODO: update db
	}
	
	@Override
	public void on() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -n %s", getInternalId()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		//TODO: update db
	}
	
	@Override
	public Boolean getValue() {
		return null;
	}
	
	@Override
	public DatabaseTable getTableDesign() {
		DatabaseTable table = new DatabaseTable(2);
		table.setColumn(0, "Registered", ColumnType.DateTime);
		table.setColumn(1, "Value", ColumnType.Boolean);
		return table;
	}
	
	@Override
	public Object[] getDataRow() {
		return new Object[] { new Date(), getValue() };
	}
}
