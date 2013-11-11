package com.homeki.core.conditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.homeki.core.events.Event;
import com.homeki.core.events.SpecialValueChangedEvent;

@Entity
public class SpecialValueCondition extends Condition {
	@Column
	private String source;
	
	@Column
	private int value;
	
	@Column
	private int operator;
	
	@Transient
	private boolean status;
	
	public SpecialValueCondition() {
		
	}
	
	public SpecialValueCondition(String source, int value, int op) {
		this.source = source;
		this.value = value;
		this.operator = op;
	}
	
	@Override
	public boolean update(Event e) {
		if (e instanceof SpecialValueChangedEvent) {
			SpecialValueChangedEvent svce = (SpecialValueChangedEvent)e;
			if (svce.source.equals(source)) {
				boolean newStatus = evalute(svce.value, value, operator);
				
				if (newStatus != status) {
					status = newStatus;
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isFulfilled() {
		return status;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getOperator() {
		return operator;
	}
	
	public void setOperator(int operator) {
		this.operator = operator;
	}
	
	@Override
	public String getType() {
		return "specialvalue";
	}
}
