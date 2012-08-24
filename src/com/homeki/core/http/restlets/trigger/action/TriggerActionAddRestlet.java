package com.homeki.core.http.restlets.trigger.action;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ChangeChannelValueAction;
import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonAction;
import com.homeki.core.http.json.JsonChangeChannelValueAction;
import com.homeki.core.triggers.Trigger;

public class TriggerActionAddRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		String type = getStringParam(c, "type");
		
		Action action = null;
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
			
		if (type.equals("changechannelvalue"))
			action = parseChangeChannelValue(c);
		else
			throw new ApiException("No such action type.");
		
		trigger.addAction(action);
		c.ses.save(action);
		
		JsonAction newid = new JsonAction();
		newid.id = action.getId();
		
		set200Response(c, gson.toJson(newid));
	}
	
	private Action parseChangeChannelValue(Container c) {
		JsonChangeChannelValueAction jact = getJsonObject(c, JsonChangeChannelValueAction.class);
		
		if (jact.number == null)
			throw new ApiException("Missing number.");
		if (jact.deviceId == null)
			throw new ApiException("Missing deviceId.");
		if (jact.channel == null)
			throw new ApiException("Missing channel.");
		
		Device dev = (Device)c.ses.get(Device.class, jact.deviceId);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		// TODO: add more validation here (does device have channel, etc)
			
		return new ChangeChannelValueAction(dev, jact.channel, jact.number);
	}
}
