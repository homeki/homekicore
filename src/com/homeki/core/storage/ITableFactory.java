package com.homeki.core.storage;

import java.lang.reflect.Type;

public interface ITableFactory {
	void ensureTables();
	IDeviceTable getDeviceTable();
	IHistoryTable getHistoryTable(String tableName, Type valueType);
}
