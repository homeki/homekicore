package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.device.Channel;

public class JsonChannel {
	public int id;
	public String name;
	public String dataType;
	
	public JsonChannel(Channel channel) {
		this.id = channel.id;
		this.name = channel.name;
		this.dataType = channel.dataType;
	}
	
	public static JsonChannel[] convertList(List<Channel> channels) {
		JsonChannel[] jsonChannels = new JsonChannel[channels.size()];
		
		for (int i = 0; i < jsonChannels.length; i++)
			jsonChannels[i] = new JsonChannel(channels.get(i));
		
		return jsonChannels;
	}
}
