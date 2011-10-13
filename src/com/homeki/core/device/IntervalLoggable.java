package com.homeki.core.device;

public interface IntervalLoggable<T> extends Queryable<T> {
	void updateValue();
}
