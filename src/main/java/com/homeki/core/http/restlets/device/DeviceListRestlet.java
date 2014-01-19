package com.homeki.core.http.restlets.device;

import com.homeki.core.device.Device;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonDevice;

import java.util.List;

public class DeviceListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		@SuppressWarnings("unchecked")
		List<Device> list = c.ses.createCriteria(Device.class).list();
		set200Response(c, gson.toJson(JsonDevice.convertList(list)));
	}
}
