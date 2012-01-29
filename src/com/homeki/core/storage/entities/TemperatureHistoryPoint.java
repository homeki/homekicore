package com.homeki.core.storage.entities;

import com.homeki.core.storage.HistoryPoint;

public class TemperatureHistoryPoint extends HistoryPoint {
	private Double value;
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
}
