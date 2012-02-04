package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.device.Device;

public class JsonDeviceLink {
	public Integer id;
	public String name;
	public Boolean linked;
	
	public JsonDeviceLink(Device d) {
		id = d.getId();
		name = d.getName();
		linked = Boolean.FALSE;
	}

	public void setLinked(boolean value) {
		linked = value;
	}
	
	public static JsonDeviceLink[] convertList(List<Device> devices) {
		JsonDeviceLink[] jsonDevices = new JsonDeviceLink[devices.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = new JsonDeviceLink(devices.get(i));
		
		return jsonDevices;
	}
}
