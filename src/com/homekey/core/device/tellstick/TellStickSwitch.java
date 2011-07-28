package com.homekey.core.device.tellstick;

import java.io.IOException;
import java.util.Date;

import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.DatabaseTable;

public class TellStickSwitch extends Device implements Switchable, Queryable<Boolean> {
	public final String TYPE = "SWITCH";
	private boolean on;
	
	public TellStickSwitch(String internalId) {
		super(internalId);
		setValue(false);
	}
	
	@Override
	public boolean off() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -f %s", internalId));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		setValue(false);
		return true;
	}
	
	@Override
	public boolean on() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -n %s", internalId));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		setValue(true);
		return true;
	}
	
	@Override
	public Boolean getValue() {
		return on;
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
		return new Object[] { new Date(), on };
	}

	@Override
	public void setValue(Boolean value) {
		on = value;
	}
}
