package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class SwitchHistoryPoint extends HistoryPoint {
	@Column(name="bool_value")
	private Boolean value;
	
	public SwitchHistoryPoint() {
		
	}
	
	public SwitchHistoryPoint(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}
}
