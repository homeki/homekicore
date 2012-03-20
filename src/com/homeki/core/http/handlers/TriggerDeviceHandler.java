package com.homeki.core.http.handlers;

import java.util.List;
import java.util.Set;

import com.homeki.core.device.Device;
import com.homeki.core.device.TimerTrigger;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTriggerDeviceLink;

public class TriggerDeviceHandler extends HttpHandler {
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
		int id = getIntParameter(c, "triggerid");
		
		@SuppressWarnings("unchecked")
		List<Device> list = c.session.createCriteria(Device.class).list();
		
		TimerTrigger trigger = (TimerTrigger)c.session.get(TimerTrigger.class, id);

		if (trigger == null)
			throw new ApiException("No timer trigger with specified ID.");
		
		Set<Device> linkedDevices = trigger.getDevices();

		JsonTriggerDeviceLink[] jsonDevices = JsonTriggerDeviceLink.convertList(list, trigger.getId());
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i].setLinked(linkedDevices.contains(list.get(i)));
		
		set200Response(c, gson.toJson(jsonDevices));
	}
}
