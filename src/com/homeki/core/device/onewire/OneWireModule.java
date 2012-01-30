package com.homeki.core.device.onewire;

import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.main.Module;

public class OneWireModule implements Module {
	private ControlledThread detectorThread;
	private ControlledThread collectorThread;
	
	public OneWireModule() {
		
	}
	
	@Override
	public void construct(ConfigurationFile file) {
		OneWireDevice.rootPath = file.getString("module.onewire.path");
		
		if (OneWireDevice.rootPath.equals("")) {
			L.e("No 1-wire root path specified in configuration file. Skipped starting 1-wire threads.");
			return;
		}

		detectorThread = new OneWireDetector(60000);
		detectorThread.start();
		
		collectorThread = new OneWireCollector(60000);
		collectorThread.start();
	}
	
	@Override
	public void destruct() {
		detectorThread.shutdown();
		collectorThread.shutdown();
	}
}
