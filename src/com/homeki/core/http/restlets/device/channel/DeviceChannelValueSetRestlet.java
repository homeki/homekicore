package com.homeki.core.http.restlets.device.channel;

import com.homeki.core.device.Device;
import com.homeki.core.device.Settable;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.KiContainer;
import com.homeki.core.http.KiRestlet;

public class DeviceChannelValueSetRestlet extends KiRestlet {
	@Override
	protected void handle(KiContainer c) {
		int id = getInt(c, "deviceid");
		int channel = getInt(c, "channelid");
		int value = getIntParam(c, "value");

		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		if (!(dev instanceof Settable))
			throw new ApiException("Device with specified ID is not a settable.");

		((Settable)dev).set(channel, value);
		
		set200Response(c, "Device state successfully changed.");
	}
}
