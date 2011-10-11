package com.homekey.core.storage;

import java.util.Date;

public class DatumPoint {
	public Date registered;
	public Object value;
	
	public DatumPoint(Date registered, Object value) {
		this.registered = registered;
		this.value = value;
	}
}
