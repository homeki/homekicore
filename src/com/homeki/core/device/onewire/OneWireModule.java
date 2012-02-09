package com.homeki.core.device.onewire;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;

public class OneWireModule implements Module {
	private ControlledThread detectorThread;
	private ControlledThread collectorThread;
	
	public OneWireModule() {
		
	}
	
	@Override
	public void construct() {		
		detectorThread = new OneWireDetector();
		detectorThread.start();
		
		collectorThread = new OneWireCollector();
		collectorThread.start();
	}
	
	@Override
	public void destruct() {
		detectorThread.shutdown();
		collectorThread.shutdown();
	}
}
