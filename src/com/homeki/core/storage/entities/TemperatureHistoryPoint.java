package com.homeki.core.storage.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.homeki.core.storage.HistoryPoint;

@Entity
public class TemperatureHistoryPoint extends HistoryPoint {
	@Column(name="double_value")
	private Double value;
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
}
