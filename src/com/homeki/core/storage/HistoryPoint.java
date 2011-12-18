package com.homeki.core.storage;

import java.util.Date;

public interface HistoryPoint {
	Date getRegistered();
	Object getValue();
}
