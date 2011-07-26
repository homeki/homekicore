package com.homekey.core.main;

import java.util.HashMap;
import java.util.Map;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.command.GetStatusCommand;
import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;

public class Monitor {
	private String name;
	Map<Integer, Device> devices;
	private CommandsThread ct;
	
	public Monitor(CommandsThread ct) {
		this.ct = ct;
		devices = new HashMap<Integer, Device>();
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
		if (devices.containsKey(i)) {
			return devices.get(i);
		}
		throw new NullPointerException();
	}
	
	public String getStatus(Queryable q) {
		GetStatusCommand gsc = new GetStatusCommand(q);
		ct.post(gsc);
		return gsc.getResult();
	}
	
	public String turnOn(Switchable s) {
		SwitchDeviceCommand sdc = new SwitchDeviceCommand(s, true);
		ct.post(sdc);
		return String.valueOf(sdc.getResult());
	}
	
	public String turnOff(Switchable s) {
		SwitchDeviceCommand sdc = new SwitchDeviceCommand(s, false);
		ct.post(sdc);
		return String.valueOf(sdc.getResult());
	}
	
	public synchronized String getDevices() {
		
		return "hej hej";
	}
}
