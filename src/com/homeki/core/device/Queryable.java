package com.homeki.core.device;

import java.util.Date;
import java.util.List;

import com.homeki.core.storage.DatumPoint;

public interface Queryable<T> {
	public T getValue();
	List<DatumPoint> getHistory(Date from, Date to);
}
