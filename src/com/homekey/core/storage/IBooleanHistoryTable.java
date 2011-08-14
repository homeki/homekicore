package com.homekey.core.storage;

import java.util.Date;

public interface IBooleanHistoryTable {
	void ensureTable();
	void putValue(Date date, boolean value);
	Boolean getLatestValue();
}
