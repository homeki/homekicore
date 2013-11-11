package com.homeki.core.main;

import com.homeki.core.logging.L;

public class BroadcastModule implements Module {
	private ControlledThread broadcastThread;
	
	@Override
	public void construct() {
		try {
			broadcastThread = new BroadcastListenerThread();
			broadcastThread.start();
		} catch (Exception e) {
			L.e("Could not start BroadcastListenerThread.", e);
		}
	}

	@Override
	public void destruct() {
		if (broadcastThread != null)
			broadcastThread.shutdown();
	}
}
