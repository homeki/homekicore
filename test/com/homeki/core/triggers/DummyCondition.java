package com.homeki.core.triggers;

import com.homeki.core.conditions.Condition;
import com.homeki.core.events.Event;

public class DummyCondition extends Condition {
	private final boolean state;

	public DummyCondition(boolean state){
		this.state = state;
	}

	@Override
	public boolean check(Event e) {
		return state;
	}
}
