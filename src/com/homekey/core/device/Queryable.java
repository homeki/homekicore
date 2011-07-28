package com.homekey.core.device;

public interface Queryable<T> {
	public T getValue();
	public void setValue(T value);
}
