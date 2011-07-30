package com.homekey.core.device;

import com.homekey.core.device.mock.MockDimmerDevice;
import com.homekey.core.device.mock.MockSwitchDevice;
import com.homekey.core.device.onewire.OneWireTemperatureDevice;
import com.homekey.core.device.tellstick.TellStickDimmer;
import com.homekey.core.device.tellstick.TellStickSwitch;
import com.homekey.core.storage.Database;

public class DeviceFactory {	
	public static Device createDevice(Database db, DeviceInformation di) {
		if (di.getType() == OneWireTemperatureDevice.class) {
			String deviceDirPath = di.getAdditionalData("deviceDirPath");
			return new OneWireTemperatureDevice(di.getInternalId(), db, deviceDirPath);
		}
		else if (di.getType() == TellStickSwitch.class) {
			return new TellStickSwitch(di.getInternalId(), db);
		}
		else if (di.getType() == TellStickDimmer.class) {
			return new TellStickDimmer(di.getInternalId(), db);
		}
		else if (di.getType() == MockSwitchDevice.class) {
			return new MockSwitchDevice(di.getInternalId(), db);	
		}
		else if (di.getType() == MockDimmerDevice.class) {
			return new MockDimmerDevice(di.getInternalId(), db);	
		}
		
		throw new IllegalArgumentException("Corresponding device class not found.");
	}
}
