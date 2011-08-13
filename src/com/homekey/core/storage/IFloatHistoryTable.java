package com.homekey.core.storage;

import java.util.Date;

public interface IFloatHistoryTable {
	void ensureTable(String tableName);
	void putValue(Date date, float value);
}
