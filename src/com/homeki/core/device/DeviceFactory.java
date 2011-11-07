package com.homeki.core.device;

import com.homeki.core.device.camera.Camera;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.device.onewire.OneWireThermometer;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.log.L;
import com.homeki.core.storage.ITableFactory;

public class DeviceFactory {
	public static Device createDevice(ITableFactory factory, DeviceInformation di) {
		if (di.getType() == OneWireThermometer.class) {
			String deviceDirPath = di.getAdditionalData("deviceDirPath");
			return new OneWireThermometer(di.getInternalId(), factory, deviceDirPath);
		} else if (di.getType() == TellStickSwitch.class) {
			return new TellStickSwitch(di.getInternalId(), factory);
		} else if (di.getType() == TellStickDimmer.class) {
			return new TellStickDimmer(di.getInternalId(), factory);
		} else if (di.getType() == MockSwitch.class) {
			return new MockSwitch(di.getInternalId(), factory);
		} else if (di.getType() == MockDimmer.class) {
			return new MockDimmer(di.getInternalId(), factory);
		} else if (di.getType() == MockSwitch.class) {
			return new MockSwitch(di.getInternalId(), factory);
		} else if (di.getType() == MockDimmer.class) {
			return new MockDimmer(di.getInternalId(), factory);
		} else if (di.getType() == MockThermometer.class) {
			return new MockThermometer(di.getInternalId(), factory);
		}else if (di.getType() == Camera.class) {
			return new Camera(di.getInternalId(),di.getAdditionalData("nick"), factory);
		}
		
		L.e("Corresponding device class not found for DeviceInformation in DeviceFactory.");
		// TODO: Why do we not throw an Exception here?
		return null;
	}
}
