package com.homeki.core.conditions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.events.Event;
import com.homeki.core.triggers.Trigger;

@Entity
public class ConditionGroup extends Condition {
	@OneToMany(mappedBy = "condition", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<Condition> conditions;
	
	@OneToMany(mappedBy = "conditionGroup")
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
		condition.setParent(this);
		conditions.add(condition);
	}
	
	public List<Condition> getConditions() {
		return conditions;
	}

	@Override
	public String getType() {
		return "conditiongroup";
	}

	public void deleteCondition(Condition condition) {
		condition.setParent(null);
		conditions.remove(condition);
	}
}
