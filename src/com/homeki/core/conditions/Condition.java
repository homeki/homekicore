package com.homeki.core.conditions;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.homeki.core.events.Event;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Condition {
	public static final int EQ = 0;
	public static final int LT = 1;
	public static final int GT = 2;
	public static final int IGNORE = 3;
	
	@Transient
	protected boolean status;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "condition_id")
	private Condition condition;
	
	public abstract boolean check(Event e);
	
	public void setParent(Condition condition) {
		this.condition = condition;
	}
	
	protected boolean evalute(Number value, Number checkValue, int operator) {
		int v = Double.compare(value.doubleValue(), checkValue.doubleValue());
		switch (operator) {
		case EQ:
			return v == 0;
		case LT:
			return v < 0;
		case GT:
			return v > 0;
		case IGNORE:
			return true;
		}
		return false;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract String getType();
}
