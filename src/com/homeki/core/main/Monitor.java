package com.homeki.core.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.IntervalLoggable;

public class Monitor {
	private Map<Integer, Device> devices;
	
	public Monitor() {
		devices = new HashMap<Integer, Device>();
	}
	
	public synchronized void addDevice(Device dev) {
		devices.put(dev.getId(), dev);
	}
	
	public synchronized Device getDevice(int id) {
		if (devices.containsKey(id)) {
			return devices.get(id);
		}
		throw new NoSuchElementException("No device with id " + id + ".");
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
