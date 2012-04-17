package com.homeki.core.http.handlers;

import java.util.List;
import java.util.Set;

import com.homeki.core.device.Device;
import com.homeki.core.device.Trigger;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTriggerDeviceLink;

public class DeviceTriggerHandler extends HttpHandler {
	public enum Actions {
		BAD_ACTION, LIST
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "trigger"
		c.path.nextToken(); // dismiss "timer"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case LIST:
			resolveList(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}

	private void resolveList(Container c) {
		int id = getIntParameter(c, "deviceid");
		
		@SuppressWarnings("unchecked")
		
		List<Trigger> list = c.session.createCriteria(Trigger.class).list();

		Device device = (Device)c.session.get(Device.class, id);

		if (device == null)
			throw new ApiException("No device with specified ID.");
		
		Set<Trigger> linkedTriggers = device.getTriggers();

		JsonTriggerDeviceLink[] jsonDevices = JsonTriggerDeviceLink.convertList(device.getId(), list);
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i].setLinked(linkedTriggers.contains(list.get(i)));
		
		set200Response(c, gson.toJson(jsonDevices));
	}
}
