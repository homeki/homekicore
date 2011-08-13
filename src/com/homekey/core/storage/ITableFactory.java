package com.homekey.core.storage;

public interface ITableFactory {
	void ensureTables();
	IDeviceTable getDeviceTable();
	IFloatHistoryTable getFloatHistoryTable(String tableName);
	IBooleanHistoryTable getBoolHistoryTable(String tableName);
	IIntegerHistoryTable getIntegerHistoryTable(String tableName);
}
