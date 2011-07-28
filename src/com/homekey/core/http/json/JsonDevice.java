package com.homekey.core.http.json;

import java.util.Date;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.homekey.core.device.Device;

public class JsonDevice {
	public String type;
	public int id;
	public String name;
	public Date added;
	public boolean active;
	
	public JsonDevice(Device d){
		type = d.getClass().getSimpleName();
		id = d.getId();
		name = d.getName();
		added = d.getAdded();
		active = d.isActive();
	}

	public static JsonDevice[] makeArray(Device[] devices) {
		JsonDevice[] jsonDevices = new JsonDevice[devices.length];
		for (int i = 0; i < jsonDevices.length; i++) {
			jsonDevices[i] = new JsonDevice(devices[i]);
		}
		return jsonDevices;
	}
}
