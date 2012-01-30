package com.homeki.core.http.json;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;

public class JsonDevice {
	public String type;
	public Integer id;
	public String name;
	public String description;
	public Date added;
	
	public JsonDevice(Device d) {
		type = d.getType();
		id = d.getId();
		name = d.getName();
		added = d.getAdded();
	}

	public static JsonDevice[] convertList(List<Device> devices) {
		JsonDevice[] jsonDevices = new JsonDevice[devices.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = new JsonDevice(devices.get(i));
		
		return jsonDevices;
	}
}
