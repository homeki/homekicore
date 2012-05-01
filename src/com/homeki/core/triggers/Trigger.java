package com.homeki.core.triggers;

import javax.persistence.Entity;

import org.hibernate.Session;

import com.homeki.core.actions.Action;
import com.homeki.core.events.Event;
import com.homeki.core.events.EventCondition;


@Entity
public class Trigger {
	EventCondition eventCondition;
	Action action;
	
	public boolean check(Event e){
		return eventCondition.check(e);
	}
	
	public void execute(Session ses){
		action.execute(ses);
	}
}
