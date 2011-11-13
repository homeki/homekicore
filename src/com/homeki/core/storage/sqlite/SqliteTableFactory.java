package com.homeki.core.storage.sqlite;

import java.lang.reflect.Type;

import com.homeki.core.storage.IDeviceTable;
import com.homeki.core.storage.IHistoryTable;
import com.homeki.core.storage.ISettingsTable;
import com.homeki.core.storage.ITableFactory;

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

	@Override
	public ISettingsTable getSettingsTable() {
		return new SqliteSettingsTable(databasePath);
	}	
}
