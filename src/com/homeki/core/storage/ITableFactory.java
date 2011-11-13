package com.homeki.core.storage;

import java.lang.reflect.Type;

public interface ITableFactory {
	void ensureTables();
	void upgrade(String version);
	IDeviceTable getDeviceTable();
	IHistoryTable getHistoryTable(String tableName, Type valueType);
	ISettingsTable getSettingsTable();
}
