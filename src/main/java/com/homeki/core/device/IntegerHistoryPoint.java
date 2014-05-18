package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("1")
public class IntegerHistoryPoint extends HistoryPoint {
	@Column(name="int_value")
	private int value;
	
	public IntegerHistoryPoint() {
		super(0);
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
}
