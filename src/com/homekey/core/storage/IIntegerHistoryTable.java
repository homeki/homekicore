package com.homekey.core.storage;

import java.util.Date;

public interface IIntegerHistoryTable {
	void ensureTable(String tableName);
	void putValue(Date date, int value);
}
