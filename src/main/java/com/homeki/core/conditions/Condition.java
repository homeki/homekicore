package com.homeki.core.conditions;

import com.homeki.core.events.Event;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Condition {
	public static final int EQ = 0;
	public static final int LT = 1;
	public static final int GT = 2;
	public static final int IGNORE = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "condition_id")
	private Condition condition;
	
	public abstract boolean update(Event e);
	public abstract boolean isFulfilled();
	
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
