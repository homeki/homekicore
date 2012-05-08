package com.homeki.core.triggers;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

import com.homeki.core.actions.Action;
import com.homeki.core.conditions.Condition;
import com.homeki.core.events.Event;


@Entity
public class Trigger {
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "condition_id")
	private Condition eventCondition;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "action_id")
	private Action action;
	
	public Trigger(Condition c, Action a) {
		eventCondition = c;
		action = a;
	}

	public boolean check(Event e){
		return eventCondition.check(e);
	}
	
	public void execute(Session ses){
		action.execute(ses);
	}
}
