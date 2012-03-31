package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class IntegerHistoryPoint extends HistoryPoint {
	@Column(name="int_value")
	private int value;
	
	public IntegerHistoryPoint() {
		super(0);
	}
	
	public IntegerHistoryPoint(int value) {
		super(0);
		this.value = value;
	}
	
	public IntegerHistoryPoint(int channel, int value) {
		super(channel);
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
}
