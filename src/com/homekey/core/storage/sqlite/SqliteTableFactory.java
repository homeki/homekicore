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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBooleanHistoryTable getBoolHistoryTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIntegerHistoryTable getIntegerHistoryTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}	
}
