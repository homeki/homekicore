package com.homeki.core.conditions;

import javax.persistence.Column;
import javax.persistence.Entity;

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
	
	public SpecialValueCondition() {
		
	}
	
	public SpecialValueCondition(String source, int value, int op) {
		this.source = source;
		this.value = value;
		this.operator = op;
	}
	
	public boolean check(Event e) {
		if (e instanceof SpecialValueChangedEvent) {
			SpecialValueChangedEvent svce = (SpecialValueChangedEvent)e;
			if (svce.source.equals(source))
				status = evalute(svce.value, value, operator);
		}
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