package com.homeki.core.triggers;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	private Condition condition;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "action_id")
	private Action action;
	
	@Column
	private String name;
	
	public Trigger() {
		
	}

	public boolean check(Event e){
		return condition.check(e);
	}
	
	public void execute(Session ses){
		action.execute(ses);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}
}
