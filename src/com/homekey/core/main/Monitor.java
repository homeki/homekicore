package com.homekey.core.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.homekey.core.device.Device;
import com.homekey.core.device.IntervalLoggable;

public class Monitor {
	private Map<Integer, Device> devices;
	
	public Monitor() {
		devices = new HashMap<Integer, Device>();
	}
	
	public void addDevice(Device dev) {
		devices.put(dev.getId(), dev);
	}
	
	public List<IntervalLoggable<?>> getLoggableDevices() {
		//TODO: implement
		return null;
	}
	
	public Device getDevice(int i) {
		if (devices.containsKey(i)) {
			return devices.get(i);
		}
		return null;
	}
	
	public Device[] getDevices() {
		return devices.values().toArray(new Device[devices.size()]);
	}
}
