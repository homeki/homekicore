package com.homeki.core.http.handlers;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;

public class DeviceHandler extends HttpHandler {
	public enum Actions {
		LIST, SET, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "device"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception ignore) {}
		
		switch (action) {
		case LIST:
			resolveList(c);
			break;
		case SET:
			resolveSet(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveSet(Container c) {
		int id = getIntParameter(c, "deviceid");
		String post = getPost(c);
		
		JsonDevice jdev = gson.fromJson(post, JsonDevice.class);
		
		Device dev = (Device)c.ses.get(Device.class, id);
		
		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);
		
		c.ses.save(dev);
		
		set200Response(c, "Device updated successfully.");
	}
	
	private void resolveList(Container c) {
		@SuppressWarnings("unchecked")
		List<Device> list = c.ses.createCriteria(Device.class).list();
		set200Response(c, gson.toJson(JsonDevice.convertList(list, c.ses)));
	}
}
