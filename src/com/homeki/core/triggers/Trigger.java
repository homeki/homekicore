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
import com.homeki.core.events.Event;
import com.homeki.core.events.EventCondition;


@Entity
public class Trigger {
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "event_condition_id")
	private EventCondition eventCondition;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "action_id")
	private Action action;
	
	public boolean check(Event e){
		return eventCondition.check(e);
	}
	
	public void execute(Session ses){
		action.execute(ses);
	}
}
