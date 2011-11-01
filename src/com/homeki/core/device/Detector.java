package com.homeki.core.device;

import java.util.List;

public abstract class Detector {
	public abstract List<DeviceInformation> findDevices();
	
	public String toString() {
		return "Detector[" + this.getClass().toString() + "]";
	}
}
