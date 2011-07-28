package com.homekey.core.device.mock;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;

public class MockDetector extends Detector {
	private MockDeviceSwitcher switch1;
	private MockDeviceSwitcher switch2;
	private MockDeviceDimmer dimmer1;
	
	public MockDetector() {
		switch1 = new MockDeviceSwitcher("switch1", true);
		switch2 = new MockDeviceSwitcher("switch2", true);
		dimmer1 = new MockDeviceDimmer("dimmer1", true);
	}
	
	@Override
	public Device[] findDevices() {
		return new Device[] { switch1, switch2, dimmer1 };
	}
}
