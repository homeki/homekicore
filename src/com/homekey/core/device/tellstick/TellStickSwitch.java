package com.homekey.core.device.tellstick;

import java.io.IOException;
import java.util.Date;

import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.log.L;
import com.homekey.core.storage.IBooleanHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class TellStickSwitch extends Device implements Switchable, Queryable<Boolean> {
	IBooleanHistoryTable boolHistoryTable;
	
	public TellStickSwitch(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}
	
	@Override
	public void off() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -f %s", getInternalId()));
		} catch (IOException e) {
			L.e("Couldn't send command using tdtool.");
			return;
		}
		
		boolHistoryTable.putValue(new Date(), false);
	}
	
	@Override
	public void on() {
		try {
			Runtime.getRuntime().exec(String.format("tdtool -n %s", getInternalId()));
		} catch (IOException e) {
			L.e("Couldn't send command using tdtool.");
			return;
		}
		
		boolHistoryTable.putValue(new Date(), true);
	}
	
	@Override
	public Boolean getValue() {
		return boolHistoryTable.getLatestValue();
	}

	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		boolHistoryTable = factory.getBoolHistoryTable(tableName);
	}
}
