package com.homeki.core.json.devices;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.json.JsonChannel;

import java.util.Date;
import java.util.List;

@JsonTypeName("unspecified")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "vendor", defaultImpl = JsonDevice.class)
@JsonSubTypes({
								@JsonSubTypes.Type(value = JsonTellStickDevice.class, name = "tellstick"),
								@JsonSubTypes.Type(value = JsonMockDevice.class, name = "mock"),
})
public class JsonDevice {
	public String vendor;
	public String type;
	public Integer deviceId;
	public String name;
	public String description;
	public Date added;
	public Boolean active;
	public JsonChannel[] channelValues;

	public JsonDevice() {
		
	}
	
	public JsonDevice(Device d) {
		type = d.getType();
		deviceId = d.getId();
		name = d.getName();
		added = d.getAdded();
		active = d.isActive();
		description = d.getDescription();

		List<Channel> channels = d.getChannels();
		channelValues = new JsonChannel[channels.size()];
		
		for (int i = 0; i < channels.size(); i++) {
			int channelId = channels.get(i).getId();
			HistoryPoint p = d.getLatestHistoryPoint(channelId);
			channelValues[i] = new JsonChannel(channelId, p.getValue());
		}
	}

	public static JsonDevice[] convertList(List<Device> devices) {
		JsonDevice[] jsonDevices = new JsonDevice[devices.size()];
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i] = devices.get(i).toJson();
		
		return jsonDevices;
	}
}
