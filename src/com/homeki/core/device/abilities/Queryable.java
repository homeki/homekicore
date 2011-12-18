package com.homeki.core.device.abilities;

import java.util.Date;
import java.util.List;

import com.homeki.core.storage.HistoryPoint;

public interface Queryable<T> {
	public T getValue();
	List<HistoryPoint> getHistory(Date from, Date to);
}
