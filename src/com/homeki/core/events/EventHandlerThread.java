package com.homeki.core.events;

import com.homeki.core.main.ControlledThread;

public class EventHandlerThread extends ControlledThread {
	public EventHandlerThread() {
		super(0);
	}

	protected void iteration() throws Exception {
		Event e = EventQueue.getInstance().pop(); // will block until event received
		
		
	}
}
