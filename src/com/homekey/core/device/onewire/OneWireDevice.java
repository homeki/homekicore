package com.homekey.core.device.onewire;

import com.homekey.core.device.Device;

public abstract class OneWireDevice extends Device {
	protected String deviceDirPath;
	
	public OneWireDevice(String internalId, String deviceDirPath) {
		super(internalId);
		
	}
	
}
