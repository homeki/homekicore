package com.homekey.core.storage;

import java.util.Date;

public interface IIntegerHistoryTable {
	void ensureTable();
	void putValue(Date date, int value);
}
