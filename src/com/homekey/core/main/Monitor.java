package com.homekey.core.main;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.homekey.core.command.Command;
import com.homekey.core.command.GetStatusCommand;
import com.homekey.core.command.SwitchDeviceCommand;
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
		return null;
	}
	
	public synchronized String getStatus(Queryable q) {
		return new GetStatusCommand(q).postAndWaitForResult(this);
	}
	
	public synchronized String getDevices() {
		JsonObject jo = new JsonObject();
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		for (Device d : devices.values())
			jo.add(d.getClass().getSimpleName(), g.toJsonTree(d));
		return g.toJson(jo);
	}
	
	// Should not be synchronized, since PQ is thread-safe.
	public void post(Command<?> c) {
		workQueue.add(c);
	}
	
	// Should not be synchronized, since PQ is thread-safe.
	public Command<?> takeCommand() throws InterruptedException {
		return workQueue.take();
	}

	public Boolean flip(Switchable s, boolean b) {
		return new SwitchDeviceCommand(s, b).postAndWaitForResult(this);
	}
}
