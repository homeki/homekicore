package com.homeki.core.http.restlets.trigger.action;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ChangeChannelValueAction;
import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonChangeChannelValueAction;
import com.homeki.core.triggers.Trigger;

public class TriggerActionSetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		int actionId = getInt(c, "actionid");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		Action act = (Action)c.ses.get(Action.class, actionId);
		
		if (act == null)
			throw new ApiException("No action with the specified ID found.");
		
		if (act instanceof ChangeChannelValueAction)
			parseChangeChannelAction(c, (ChangeChannelValueAction)act);
		
		c.ses.save(act);
		set200Response(c, msg("Action updated successfully."));
	}
	
	private void parseChangeChannelAction(Container c, ChangeChannelValueAction act) {
		JsonChangeChannelValueAction jact = getJsonObject(c, JsonChangeChannelValueAction.class);
		
		if (jact.deviceId != null) {
			Device dev = (Device)c.ses.get(Device.class, jact.deviceId);
			
			if (dev == null)
				throw new ApiException("Could not load new device from device ID.");
			
			act.setDevice(dev);
		}
		if (jact.channel != null) {
			act.setChannel(jact.channel);
		}
		if (jact.value != null) {
			act.setValue(jact.value);
		}
	}
}
