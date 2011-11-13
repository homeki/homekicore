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
		getSettingsTable().ensureTable();
		getDeviceTable().ensureTable();
	}

	@Override
	public void upgrade(String version) {
		ISettingsTable settingsTable = getSettingsTable();
		
		if (settingsTable.getString("version").isEmpty())
			settingsTable.setString("version", version);
		
		String fromVersion = settingsTable.getString("version");
		
		SqliteDatabaseUpgrader upg = new SqliteDatabaseUpgrader(databasePath, fromVersion);
		upg.execute();
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
