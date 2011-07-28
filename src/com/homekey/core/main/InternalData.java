package com.homekey.core.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.homekey.core.device.Device;
import com.homekey.core.http.json.JsonDevice;

public class InternalData {
	private String name;
	private Map<Integer, Device> devices;
	
	public InternalData() {
		devices = new HashMap<Integer, Device>();
		name = "<no name>";
	}
	
	public void setServerName(String name) {
		this.name = name;
	}
	
	public String getServerName() {
		return name;
	}
	
	public void addDevice(Device dev) {
		devices.put(dev.getId(), dev);
	}
	
	public Device getDevice(int i) {
		if (devices.containsKey(i)) {
			return devices.get(i);
		}
		return null;
	}
	
	public boolean containsDevice(Device dev) {
		return devices.containsKey(dev.getId());
	}
	
	public Device[] getDevices() {
		return devices.values().toArray(new Device[devices.size()]);
	}
}
