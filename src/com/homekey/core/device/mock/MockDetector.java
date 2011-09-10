package com.homekey.core.device.mock;

import java.util.ArrayList;
import java.util.List;

import com.homekey.core.device.Detector;
import com.homekey.core.device.DeviceInformation;

public class MockDetector extends Detector {
	
	@Override
	public List<DeviceInformation> findDevices() {
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		devices.add(new DeviceInformation("switch1", MockSwitchDevice.class));
		devices.add(new DeviceInformation("switch2", MockSwitchDevice.class));
		devices.add(new DeviceInformation("dimmer1", MockDimmerDevice.class));
		devices.add(new DeviceInformation("temp1", MockTemperatureDevice.class));
		devices.add(new DeviceInformation("temp2", MockTemperatureDevice.class));
		return devices;
	}
}
