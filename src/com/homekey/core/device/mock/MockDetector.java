package com.homekey.core.device.mock;

import java.util.ArrayList;
import java.util.List;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;

public class MockDetector extends Detector {
	
	@Override
	public List<Device> findDevices() {
		List<Device> devices = new ArrayList<Device>();
		devices.add(new MockDeviceSwitcher("switch1", true));
		devices.add(new MockDeviceSwitcher("switch2", true));
		devices.add(new MockDeviceDimmer("dimmer1", true));;
		return devices;
	}
}
