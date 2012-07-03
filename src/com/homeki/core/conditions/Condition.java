package com.homeki.core.conditions;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.events.Event;
import com.homeki.core.triggers.Trigger;

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
	
	@OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<Trigger> triggers;
	
	public abstract boolean check(Event e);
	
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
	
	@Override
	public abstract String toString();
}
