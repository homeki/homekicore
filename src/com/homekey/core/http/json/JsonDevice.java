package com.homekey.core.http.json;

import java.util.Date;

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
}
