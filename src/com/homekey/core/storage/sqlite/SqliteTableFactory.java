package com.homekey.core.storage.sqlite;

import com.homekey.core.storage.IBooleanHistoryTable;
import com.homekey.core.storage.IDeviceTable;
import com.homekey.core.storage.IFloatHistoryTable;
import com.homekey.core.storage.IIntegerHistoryTable;
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
	public IFloatHistoryTable getFloatHistoryTable(String tableName) {
		return new SqliteFloatHistoryTable(databasePath, tableName);
	}

	@Override
	public IBooleanHistoryTable getBoolHistoryTable(String tableName) {
		return new SqliteBoolHistoryTable(databasePath, tableName);
	}

	@Override
	public IIntegerHistoryTable getIntegerHistoryTable(String tableName) {
		return new SqliteIntegerHistoryTable(databasePath, tableName);
	}	
}
