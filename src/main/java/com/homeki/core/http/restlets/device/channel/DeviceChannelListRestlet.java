package com.homeki.core.http.restlets.device.channel;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonChannel;

public class DeviceChannelListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int deviceId = getInt(c, "deviceid");
		
		Device dev = (Device)c.ses.get(Device.class, deviceId);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		set200Response(c, gson.toJson(JsonChannel.convertList(dev.getChannels())));
	}
}
