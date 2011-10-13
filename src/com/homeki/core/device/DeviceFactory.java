package com.homeki.core.device;

import com.homeki.core.device.mock.MockDimmerDevice;
import com.homeki.core.device.mock.MockSwitchDevice;
import com.homeki.core.device.mock.MockTemperatureDevice;
import com.homeki.core.device.onewire.OneWireTemperatureDevice;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.log.L;
import com.homeki.core.storage.ITableFactory;

public class DeviceFactory {
	public static Device createDevice(ITableFactory factory, DeviceInformation di) {
		if (di.getType() == OneWireTemperatureDevice.class) {
			String deviceDirPath = di.getAdditionalData("deviceDirPath");
			return new OneWireTemperatureDevice(di.getInternalId(), factory, deviceDirPath);
		} else if (di.getType() == TellStickSwitch.class) {
			return new TellStickSwitch(di.getInternalId(), factory);
		} else if (di.getType() == TellStickDimmer.class) {
			return new TellStickDimmer(di.getInternalId(), factory);
		} else if (di.getType() == MockSwitchDevice.class) {
			return new MockSwitchDevice(di.getInternalId(), factory);
		} else if (di.getType() == MockDimmerDevice.class) {
			return new MockDimmerDevice(di.getInternalId(), factory);
		} else if (di.getType() == MockSwitchDevice.class) {
			return new MockSwitchDevice(di.getInternalId(), factory);
		} else if (di.getType() == MockDimmerDevice.class) {
			return new MockDimmerDevice(di.getInternalId(), factory);
		} else if (di.getType() == MockTemperatureDevice.class) {
			return new MockTemperatureDevice(di.getInternalId(), factory);
		}
		
		L.e("Corresponding device class not found for DeviceInformation in DeviceFactory.");
		return null;
	}
}
