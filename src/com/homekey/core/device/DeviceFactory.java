package com.homekey.core.device;

import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.device.mock.MockDeviceSwitcher;
import com.homekey.core.device.onewire.OneWireTemperatureSensor;
import com.homekey.core.device.tellstick.TellStickDimmer;
import com.homekey.core.device.tellstick.TellStickSwitch;
import com.homekey.core.storage.Database;

public class DeviceFactory {	
	public static Device createDevice(Database db, DeviceInformation di) {
		if (di.getType() == OneWireTemperatureSensor.class) {
			String deviceDirPath = di.getAdditionalData("deviceDirPath");
			return new OneWireTemperatureSensor(di.getInternalId(), db, deviceDirPath);
		}
		else if (di.getType() == TellStickSwitch.class) {
			return new TellStickSwitch(di.getInternalId(), db);
		}
		else if (di.getType() == TellStickDimmer.class) {
			return new TellStickDimmer(di.getInternalId(), db);
		}
		else if (di.getType() == MockDeviceSwitcher.class) {
			return new MockDeviceSwitcher(di.getInternalId(), db);	
		}
		else if (di.getType() == MockDeviceDimmer.class) {
			return new MockDeviceDimmer(di.getInternalId(), db);	
		}
		
		throw new IllegalArgumentException("Corresponding device class not found.");
	}
}
