package com.homeki.core.events;

import com.homeki.core.logging.L;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class EventHandlerModule implements Module {
	private ControlledThread eventHandlerThread;
	
	@Override
	public void construct() {
		try {
			eventHandlerThread = new EventHandlerThread();
			eventHandlerThread.start();
		} catch (Exception e) {
			L.e("Could not start EventHandlerThread.", e);
		}
	}

	@Override
	public void destruct() {
		if (eventHandlerThread != null)
			eventHandlerThread.shutdown();
	}
}
