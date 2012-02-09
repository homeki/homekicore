package com.homeki.core.device.tellstick;

import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	private ControlledThread detectorThread;
	
	public TellStickModule() {

	}
	
	@Override
	public void construct() {
		TellStickNative.open();
		
		detectorThread = new TellStickDetector(Configuration.TELLSTICK_DETECTOR_INTERVAL);
		detectorThread.start();
		
		listenerThread = new TellStickListener();
		listenerThread.start();
	}

	@Override
	public void destruct() {
		listenerThread.shutdown();
		detectorThread.shutdown();
		TellStickNative.close();
	}
}
