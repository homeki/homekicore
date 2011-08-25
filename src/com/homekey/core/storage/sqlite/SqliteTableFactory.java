package com.homekey.core.storage.sqlite;

import java.lang.reflect.Type;

import com.homekey.core.storage.IDeviceTable;
import com.homekey.core.storage.IHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class SqliteTableFactory implements ITableFactory {
	private final String databasePath;
	
	public SqliteTableFactory(String databasePath) {
		this.databasePath = databasePath;
	}

	@Override
	public void ensureTables() {
		getDeviceTable().ensureTable();
	}

	@Override
	public IDeviceTable getDeviceTable() {
		return new SqliteDeviceTable(databasePath);
	}

	@Override
	public IHistoryTable getHistoryTable(String tableName, Type valueType) {
		return new SqliteHistoryTable(databasePath, tableName, valueType);
	}	
}
