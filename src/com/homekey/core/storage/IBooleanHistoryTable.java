package com.homekey.core.storage;

import java.util.Date;

public interface IBooleanHistoryTable {
	void putValue(Date date, boolean b);
	Boolean getLatestValue();
}
