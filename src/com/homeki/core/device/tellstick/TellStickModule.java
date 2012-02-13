package com.homeki.core.device.tellstick;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	
	public TellStickModule() {

	}
	
	@Override
	public void construct() {
		TellStickNative.open();
		
		listenerThread = new TellStickListener();
		listenerThread.start();
	}

	@Override
	public void destruct() {
		listenerThread.shutdown();
		TellStickNative.close();
	}
}
