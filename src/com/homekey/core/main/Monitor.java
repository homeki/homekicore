package com.homekey.core.main;

import java.util.HashMap;
import java.util.Map;

import com.homekey.core.command.getStatusCommand;
import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;

public class Monitor {
	private String name;
	Map<Integer,Device> devices;

	public Monitor() {
		devices = new HashMap<Integer,Device>();
		name = "<no name>";
		
	}

	public synchronized void setServerName(String name) {
		this.name = name;
	}

	public synchronized String getServerName() {
		return name;
	}

	public void forceAddDevice(Device dev) {
		devices.put(dev.getId(), dev);
	}

	public Device getDevice(int i) {
		if(devices.containsKey(i)){
			return devices.get(i);
		}
		return null;
	}
	
	public String getStatus(Queryable q){
		return new getStatusCommand(q).getResult();
	}
	
}
