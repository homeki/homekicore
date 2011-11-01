package com.homeki.core.device.mock;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.DeviceInformation;

public class MockDetector extends Detector {
	
	@Override
	public List<DeviceInformation> findDevices() {
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		devices.add(new DeviceInformation("switch1", MockSwitch.class));
		devices.add(new DeviceInformation("switch2", MockSwitch.class));
		devices.add(new DeviceInformation("dimmer1", MockDimmer.class));
		devices.add(new DeviceInformation("temp1", MockThermometer.class));
		devices.add(new DeviceInformation("temp2", MockThermometer.class));
		return devices;
	}
}
