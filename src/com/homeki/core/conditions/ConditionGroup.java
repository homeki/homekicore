package com.homeki.core.conditions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.events.Event;
import com.homeki.core.logging.L;
import com.homeki.core.triggers.Trigger;

@Entity
public class ConditionGroup extends Condition {
	@OneToMany(mappedBy = "condition", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<Condition> conditions;
	
	@OneToMany(mappedBy = "conditionGroup", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<Trigger> triggers;
	
	public ConditionGroup() {
		this.conditions = new ArrayList<Condition>();
		this.triggers = new HashSet<Trigger>();
	}
	
	@Override
	public boolean check(Event e) {
		for (Condition c : conditions) {
			if (!c.check(e))
				return false;
		}
		
		return true;
	}
	
	public void addCondition(Condition condition) {
		L.i("Before add: " + conditions.size());
		condition.setParent(this);
		conditions.add(condition);
		L.i("After add: " + conditions.size());
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}

	@Override
	public String getType() {
		return "conditiongroup";
	}
}
