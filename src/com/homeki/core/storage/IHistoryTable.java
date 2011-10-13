package com.homeki.core.storage;

import java.util.Date;
import java.util.List;

import com.homeki.core.http.json.JsonPair;

public interface IHistoryTable {
	void ensureTable();
	void putValue(Date date, Object value);
	Object getLatestValue();
	List<DatumPoint> getValues(Date from, Date to);
}
