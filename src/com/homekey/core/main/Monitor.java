package com.homekey.core.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
		throw new NoSuchElementException("No device with such id: " + id);
	}
	
	public synchronized List<Device> getDevices() {
		return new ArrayList<Device>(devices.values());
	}

	public synchronized boolean containsDevice(String internalId) {
		for (Device d : devices.values()) {
			if (d.getInternalId().equals(internalId)) {
				return true;
			}
		}
		
		return false;
	}

	public synchronized boolean hasDevice(int id) {
		return devices.containsKey(id);
	}
}
