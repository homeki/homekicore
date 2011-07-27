package com.homekey.core.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.homekey.core.command.Command;
import com.homekey.core.command.CommandsThread;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.command.GetStatusCommand;
import com.homekey.core.device.Device;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;

public class Monitor {
	private String name;
	Map<Integer, Device> devices;
	BlockingQueue<Command<?>> workQueue = new LinkedBlockingQueue<Command<?>>();
	
	public Monitor() {
		devices = new HashMap<Integer, Device>();
		name = "<no name>";
	}
	
	public synchronized void setServerName(String name) {
		this.name = name;
	}
	
	public synchronized String getServerName() {
		return name;
	}
	
	public synchronized void forceAddDevice(Device dev) {
		devices.put(dev.getId(), dev);
	}
	
	public synchronized Device getDevice(int i) {
		if (devices.containsKey(i)) {
			return devices.get(i);
		}
		throw new NullPointerException();
	}
	
	public synchronized String getStatus(Queryable q) {
		return new GetStatusCommand(q).postAndWaitForResult(this);
	}
	
	public synchronized Boolean turnOn(Switchable s) {
		return new SwitchDeviceCommand(s, true).postAndWaitForResult(this);
	}
	
	public synchronized Boolean turnOff(Switchable s) {
		return new SwitchDeviceCommand(s, false).postAndWaitForResult(this);
	}
	
	public synchronized String getDevices() {
		StringBuffer sb = new StringBuffer();
		Gson g = new Gson();
		for (Device d : devices.values()) {
			sb.append(g.toJson(d) + "<br>");
		}
		return sb.toString();
	}
	
	// Should not be synchronized, since PQ is thread-safe.
	public void post(Command<?> c) {
		workQueue.add(c);
	}
	
	// Should not be synchronized, since PQ is thread-safe.
	public Command<?> takeCommand() throws InterruptedException {
			return workQueue.take();
	}
}
