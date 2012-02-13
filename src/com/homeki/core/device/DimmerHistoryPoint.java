package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class DimmerHistoryPoint extends HistoryPoint {
	@Column(name="int_value")
	private Integer value;
	
	@Column(name="level")
	private Integer level;
	
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
