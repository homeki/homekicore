package com.homeki.core.device.mock;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.DeviceInformation.DeviceType;

public class MockDetector extends Detector {
	@Override
	public List<DeviceInformation> findDevices() {
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		devices.add(new DeviceInformation("switch1", DeviceType.MockSwitch));
		devices.add(new DeviceInformation("switch2", DeviceType.MockSwitch));
		devices.add(new DeviceInformation("dimmer1", DeviceType.MockDimmer));
		devices.add(new DeviceInformation("temp1", DeviceType.MockThermometer));
		devices.add(new DeviceInformation("temp2", DeviceType.MockThermometer));
		return devices;
	}
}
