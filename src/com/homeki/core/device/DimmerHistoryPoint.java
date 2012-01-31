package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class DimmerHistoryPoint extends HistoryPoint {
	@Column(name="int_value")
	private Integer value;
	
	public DimmerHistoryPoint() {
		
	}
	
	public DimmerHistoryPoint(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
}
