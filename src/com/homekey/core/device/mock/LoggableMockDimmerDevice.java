package com.homekey.core.device.mock;

import java.util.Date;

import com.homekey.core.Logs;
import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.device.Queryable;
import com.homekey.core.log.L;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;


public class LoggableMockDimmerDevice extends MockDimmerDevice implements IntervalLoggable<Integer> {
	public LoggableMockDimmerDevice(String internalId, Database db) {
		super(internalId, db);
	}

	@Override
	public void updateValue() {
		
	}
}
