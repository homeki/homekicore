package com.homeki.core.conditions;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.events.Event;
import com.homeki.core.triggers.Trigger;

@Entity
public class ConditionGroup extends Condition {
	@OneToMany(mappedBy = "condition", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<Condition> conditions;
	
	@Override
	public boolean check(Event e) {
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public String getType() {
		return "group";
	}
}
