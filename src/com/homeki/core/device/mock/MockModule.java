package com.homeki.core.device.mock;

import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.Module;
import com.homeki.core.main.Monitor;

public class MockModule implements Module {
	@Override
	public void construct(Monitor monitor, ConfigurationFile file) {
		monitor.addDevice(new MockSwitch("switch1"));
		monitor.addDevice(new MockSwitch("switch2"));
		monitor.addDevice(new MockDimmer("dimmer1"));
		monitor.addDevice(new MockDimmer("temp1"));
		monitor.addDevice(new MockDimmer("temp2"));
	}
	
	@Override
	public void destruct() {
		
	}
}
