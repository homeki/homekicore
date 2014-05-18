package com.homeki.core.json;

import com.homeki.core.device.Channel;

import java.util.List;

public class JsonChannel {
	public int id;
	public String name;
	public String dataType;
	public Object lastValue;

	public JsonChannel() {

	}
	
	public JsonChannel(Channel channel) {
		this.id = channel.getId();
		this.name = channel.getName();
		this.dataType = channel.getDataType().toString();
	}
	
	public JsonChannel(int id, Object lastValue) {
		this.id = id;
		this.lastValue = lastValue;
	}
	
	public static JsonChannel[] convertList(List<Channel> channels) {
		JsonChannel[] jsonChannels = new JsonChannel[channels.size()];
		
		for (int i = 0; i < jsonChannels.length; i++)
			jsonChannels[i] = new JsonChannel(channels.get(i));
		
		return jsonChannels;
	}
}
