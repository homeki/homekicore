package com.homeki.core.conditions;

import com.homeki.core.events.Event;
import com.homeki.core.triggers.Trigger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		this.conditions = new ArrayList<>();
		this.triggers = new HashSet<>();
	}
	
	@Override
	public boolean update(Event e) {
		boolean modified = false;
		
		for (Condition c : conditions) {
			if (c.update(e))
				modified = true;
		}
		
		return modified;
	}
	
	@Override
	public boolean isFulfilled() {
		for (Condition c : conditions) {
			if (!c.isFulfilled())
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
