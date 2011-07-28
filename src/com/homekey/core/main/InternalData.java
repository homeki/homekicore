package com.homekey.core.main;

import java.lang.reflect.Type;
import java.util.Collections;
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

	public Device[] getDevices() {
		return devices.values().toArray(null);
	}
}
