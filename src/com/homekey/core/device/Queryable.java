package com.homekey.core.device;

import java.util.Date;
import java.util.List;

import com.homekey.core.storage.DatumPoint;

public interface Queryable<T> {
	public T getValue();
	List<DatumPoint> getHistory(Date from, Date to);
}
