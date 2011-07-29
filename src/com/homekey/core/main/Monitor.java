package com.homekey.core.main;

import java.util.ArrayList;
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
	
	public synchronized void addDevice(Device dev) {
		devices.put(dev.getId(), dev);
	}
	
	public synchronized List<IntervalLoggable<?>> getLoggableDevices() {
		List<IntervalLoggable<?>> list = new ArrayList<IntervalLoggable<?>>();
		
		for (Device d : devices.values()) {
			if (d instanceof IntervalLoggable<?>) {
				list.add((IntervalLoggable<?>)d);
			}
		}
		
		return list;
	}
	
	public synchronized Device getDevice(int id) {
		if (devices.containsKey(id)) {
			return devices.get(id);
		}
		return null;
	}
	
	public synchronized List<Device> getDevices() {
		return new ArrayList<Device>(devices.values());
	}

	public synchronized boolean containsDevice(Device dev) {
		return devices.containsValue(dev);
	}
}
