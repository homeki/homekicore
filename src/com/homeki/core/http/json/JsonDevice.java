package com.homeki.core.http.json;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.device.Device;

public class JsonDevice {
	public String type;
	public Integer id;
	public String name;
	public String description;
	public Date added;
	public Boolean active;
	public JsonState state;
	
	public JsonDevice() {
		
	}
	
	public JsonDevice(Device d, Session session) {
		type = d.getType();
		id = d.getId();
		name = d.getName();
		added = d.getAdded();
		state = new JsonState(d.getState(session));
		active = d.getActive();
		description = d.getDescription();
	}

	public static JsonDevice[] convertList(List<Device> devices, Session session) {
		JsonDevice[] jsonDevices = new JsonDevice[devices.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = new JsonDevice(devices.get(i), session);
		
		return jsonDevices;
	}
}
