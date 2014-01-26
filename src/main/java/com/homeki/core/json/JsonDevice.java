package com.homeki.core.json;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;

public class JsonDevice {
	public String type;
	public Integer id;
	public String name;
	public String description;
	public Date added;
	public Boolean active;
	public JsonChannel[] channelValues;
	public String[] abilities;
	
	public JsonDevice() {
		
	}
	
	public JsonDevice(Device d) {
		type = d.getType();
		id = d.getId();
		name = d.getName();
		added = d.getAdded();
		active = d.isActive();
		description = d.getDescription();
		abilities = d.getAbilities();
		
		List<Channel> channels = d.getChannels();
		channelValues = new JsonChannel[channels.size()];
		
		for (int i = 0; i < channels.size(); i++) {
			int channelId = channels.get(i).id;
			HistoryPoint p = d.getLatestHistoryPoint(channelId);
			channelValues[i] = new JsonChannel(channelId, p.getValue());
		}
	}

	public static JsonDevice[] convertList(List<Device> devices) {
		JsonDevice[] jsonDevices = new JsonDevice[devices.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = new JsonDevice(devices.get(i));
		
		return jsonDevices;
	}
}
