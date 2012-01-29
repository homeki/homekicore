package com.homeki.core.storage.entities;

import com.homeki.core.storage.HistoryPoint;

public class SwitchHistoryPoint extends HistoryPoint {
	private Boolean value;
	
	public Boolean getValue() {
		return value;
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}
}
