package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class SwitchHistoryPoint extends HistoryPoint {
	@Column(name="int_switch_value")
	private Integer value;
	
	public SwitchHistoryPoint() {
		
	}
	
	public SwitchHistoryPoint(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
}
