package com.homeki.core.device.onewire;

import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;
import com.homeki.core.main.Monitor;

public class OneWireModule implements Module {
	private ControlledThread detectorThread;
	
	public OneWireModule() {
		
	}
	
	@Override
	public void construct(Monitor monitor, ConfigurationFile file) {
		String owRootPath = file.getString("modules.onewire.path");

		detectorThread = new OneWireDetector(10000, owRootPath, monitor);
		detectorThread.start();
	}
	
	@Override
	public void destruct() {
		detectorThread.shutdown();
	}
}
