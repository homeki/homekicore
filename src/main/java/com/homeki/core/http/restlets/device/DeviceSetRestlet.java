package com.homeki.core.http.restlets.device;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.main.Util;

public class DeviceSetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int id = getInt(c, "deviceid");
		JsonDevice jdev = getJsonObject(c, JsonDevice.class);
		
		if (Util.isNotNullAndEmpty(jdev.name))
			throw new ApiException("Attribute 'name' exists, but is empty. An empty name is not allowed for a device.");
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);
		
		c.ses.save(dev);
		
		set200Response(c, msg("Device updated successfully."));
	}
}
