package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("2")
public class DoubleHistoryPoint extends HistoryPoint {
	@Column(name="double_value")
	private Double value;
	
	public DoubleHistoryPoint() {
		super(0);
	}
	
	public DoubleHistoryPoint(Double value) {
		super(0);
		this.value = value;
	}

	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
}
