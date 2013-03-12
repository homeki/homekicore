package com.homeki.core.clientwatch;

import com.homeki.core.logging.L;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class ClientWatchModule implements Module {
	private ControlledThread clientWatchThread;
	
	@Override
	public void construct() {
		try {
			clientWatchThread = new ClientWatchThread();
			clientWatchThread.start();
		} catch (Exception e) {
			L.e("Could not start ClientWatchThread.", e);
		}
	}

	@Override
	public void destruct() {
		if (clientWatchThread != null)
			clientWatchThread.shutdown();
	}
}
