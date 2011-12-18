package com.homeki.core.device;

import com.homeki.core.device.camera.Camera;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.device.onewire.OneWireThermometer;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickFakeDimmer;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.log.L;
import com.homeki.core.storage.ITableFactory;

public class DeviceFactory {
	public static Device createDevice(ITableFactory factory, DeviceInformation di) {
		switch (di.getType()) {
		case MockSwitch:
			return new MockSwitch(di.getInternalId(), factory);
		case MockDimmer:
			return new MockDimmer(di.getInternalId(), factory);
		case MockThermometer:
			return new MockThermometer(di.getInternalId(), factory);
		case TellStickSwitch:
			return new TellStickSwitch(di.getInternalId(), factory);
		case TellStickDimmer:
			return new TellStickDimmer(di.getInternalId(), factory);
		case TellStickFakeDimmer:
			return new TellStickFakeDimmer(di.getInternalId(), factory);
		case OneWireThermometer:
			String deviceDirPath = di.getAdditionalData("deviceDirPath");
			return new OneWireThermometer(di.getInternalId(), factory, deviceDirPath);
		case Camera:
			return new Camera(di.getInternalId(), factory);
		}
		
		L.e("Corresponding device class not found for DeviceInformation in DeviceFactory.");
		return null;
	}
}
