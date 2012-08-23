package com.homeki.core.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.triggers.Trigger;

@Entity
public class ActionGroup extends Action {
	@OneToMany(mappedBy = "action", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected List<Action> actions;
	
	@OneToMany(mappedBy = "actionGroup", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<Trigger> triggers;
	
	public ActionGroup() {
		this.actions = new ArrayList<Action>();
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	@Override
	public void execute(Session ses) {
		for (Action a : actions)
			a.execute(ses);
	}
	
	public List<Action> getActions() {
		return actions;
	}
}
