package com.homeki.core.device.tellstick;

import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;
import com.homeki.core.main.Monitor;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	private ControlledThread detectorThread;
	
	public TellStickModule() {

	}
	
	@Override
	public void construct(Monitor monitor, ConfigurationFile file) {
		TellStickNative.open();
		
		detectorThread = new TellStickDetector(10000, monitor);
		detectorThread.start();
		
		listenerThread = new TellStickListener(monitor);
		listenerThread.start();
	}

	@Override
	public void destruct() {
		listenerThread.shutdown();
		detectorThread.shutdown();
		TellStickNative.close();
	}
}
