package com.homekey.core.main;

import java.util.ArrayList;
import java.util.List;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.storage.Database;

public class Monitor {
	private String name;
	List<Device> devices;

	public Monitor() {
		devices = new ArrayList<Device>();
		name = "<no name>";
		
	}

	public synchronized void setServerName(String name) {
		this.name = name;
	}

	public synchronized String getServerName() {
		return name;
	}

	public void forceAddDevice(MockDeviceDimmer mockDeviceDimmer) {
		
	}

}
