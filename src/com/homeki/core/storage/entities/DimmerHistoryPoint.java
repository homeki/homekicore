package com.homeki.core.storage.entities;

import com.homeki.core.storage.HistoryPoint;

public class DimmerHistoryPoint extends HistoryPoint {
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
}
