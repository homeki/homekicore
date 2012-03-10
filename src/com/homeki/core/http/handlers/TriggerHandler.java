package com.homeki.core.http.handlers;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.Trigger;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTimerTrigger;

public class TriggerHandler extends HttpHandler {
	public enum Actions {
		LIST, LINK, UNLINK, DELETE, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "trigger"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case LIST:
			resolveList(c);
			break;
		case DELETE:
			resolveDelete(c);
			break;
		case LINK:
			resolveLink(c);
			break;
		case UNLINK:
			resolveUnlink(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveList(Container c) {
		@SuppressWarnings("unchecked")
		List<Trigger> list = c.ses.createCriteria(Trigger.class).list();
		set200Response(c, gson.toJson(JsonTimerTrigger.convertList(list)));
	}
	
	private void resolveDelete(Container c) {
		int id = getIntParameter(c, "triggerid");

		Trigger trigger = (Trigger)c.ses.get(Trigger.class, id);
		
		if (trigger == null)
			throw new ApiException("No trigger with specified ID.");
		
		c.ses.delete(trigger);
	}
	
	private void resolveLink(Container c) {
		int deviceid = getIntParameter(c, "deviceid");
		int triggerid = getIntParameter(c, "triggerid");
		
		Device dev = (Device)c.ses.get(Device.class, deviceid);
		Trigger tri = (Trigger)c.ses.get(Trigger.class, triggerid);
		
		if (dev == null)
			throw new ApiException("No device with specified ID found.");
		else if (tri == null)
			throw new ApiException("No trigger with specified ID found.");
		else if (dev.getTriggers().contains(tri))
			throw new ApiException("The specified device and the specified trigger are already linked.");

		dev.getTriggers().add(tri);
		c.ses.save(dev);
		set200Response(c, "Specified device and specified trigger successfully linked.");
	}
	
	private void resolveUnlink(Container c) {
		int deviceid = getIntParameter(c, "deviceid");
		int triggerid = getIntParameter(c, "triggerid");

		Device dev = (Device)c.ses.get(Device.class, deviceid);
		Trigger tri = (Trigger)c.ses.get(Trigger.class, triggerid);
		
		if (dev == null)
			throw new ApiException("No device with specified ID found.");
		else if (tri == null)
			throw new ApiException("No trigger with specified ID found.");
		else if (!dev.getTriggers().contains(tri))
			throw new ApiException("Specified device does not contain the specified trigger.");
			
		dev.getTriggers().remove(tri);
		c.ses.save(dev);
		set200Response(c, "Link between specified device and specified trigger successfully removed.");
	}
}
