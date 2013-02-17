package com.homeki.core.http.restlets.device;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;

public class DeviceDeleteRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int id = getInt(c, "deviceid");
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		dev.preDelete();
		c.ses.delete(dev);
		
		set200Response(c, msg("Device successfully deleted."));
	}
}
