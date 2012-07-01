package com.homeki.core.triggers;

import org.hibernate.Session;

import com.homeki.core.actions.Action;

public class DummyAction extends Action {
	
	private boolean triggered = false;
	
	@Override
	public void execute(Session ses) {
		triggered = true;
	}
	
	public boolean getTriggered(){
		return triggered;
	}
}
