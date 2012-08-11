package com.homeki.core.http.restlets.device;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.http.KiContainer;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonDevice;

public class DeviceListRestlet extends KiRestlet {
	@Override
	protected void handle(KiContainer c) {
		@SuppressWarnings("unchecked")
		List<Device> list = c.ses.createCriteria(Device.class).list();
		set200Response(c, gson.toJson(JsonDevice.convertList(list)));
	}
}
