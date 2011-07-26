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
		return new GetStatusCommand(q).postAndWaitForResult(ct);
	}
	
	public Boolean turnOn(Switchable s) {
		return new SwitchDeviceCommand(s, true).postAndWaitForResult(ct);
	}
	
	public Boolean turnOff(Switchable s) {
		return new SwitchDeviceCommand(s, false).postAndWaitForResult(ct);
	}
	
	public synchronized String getDevices() {
		return "hej hej";
	}
}
