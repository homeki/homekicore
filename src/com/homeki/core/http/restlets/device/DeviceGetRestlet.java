package com.homeki.core.http.restlets.device;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonDevice;

public class DeviceGetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int id = getInt(c, "deviceid");
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		set200Response(c, gson.toJson(new JsonDevice(dev)));
	}
}
