package com.homeki.core.storage.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.homeki.core.storage.HistoryPoint;

@Entity
public class SwitchHistoryPoint extends HistoryPoint {
	@Column(name="bool_value")
	private Boolean value;
	
	public Boolean getValue() {
		return value;
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}
}
