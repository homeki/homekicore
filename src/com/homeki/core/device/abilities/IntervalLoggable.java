package com.homeki.core.device.abilities;

public interface IntervalLoggable<T> extends Queryable<T> {
	void updateValue();
}
