package com.homekey.core.storage;

import java.util.Date;

public interface IHistoryTable {
	void ensureTable();
	void putValue(Date date, Object value);
	Object getLatestValue();
}
