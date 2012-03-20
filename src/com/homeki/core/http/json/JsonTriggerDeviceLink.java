package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.Trigger;

public class JsonTriggerDeviceLink {
	public Integer deviceId;
	public Integer triggerId;
	public String name;
	public Boolean linked;
	
	public JsonTriggerDeviceLink(Device d, int triggerId) {
		this.triggerId = triggerId;
		deviceId = d.getId();
		name = d.getName();
		linked = Boolean.FALSE;
	}
	
	public JsonTriggerDeviceLink(Trigger t, int deviceId) {
		this.triggerId = t.getId();
		this.deviceId = deviceId;
		name = t.getName();
		linked = Boolean.FALSE;
	}

	public void setLinked(boolean value) {
		linked = value;
	}
	
	public static JsonTriggerDeviceLink[] convertList(List<Device> devices, int triggerId) {
		JsonTriggerDeviceLink[] jsonDevices = new JsonTriggerDeviceLink[devices.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = new JsonTriggerDeviceLink(devices.get(i), triggerId);
		
		return jsonDevices;
	}
	
	public static JsonTriggerDeviceLink[] convertList(int deviceId, List<Trigger> triggers) {
		JsonTriggerDeviceLink[] jsonDevices = new JsonTriggerDeviceLink[triggers.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = new JsonTriggerDeviceLink(triggers.get(i), deviceId);
		
		return jsonDevices;
	}
}
