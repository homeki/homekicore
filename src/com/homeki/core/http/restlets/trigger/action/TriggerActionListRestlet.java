package com.homeki.core.http.restlets.trigger.action;

import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonAction;
import com.homeki.core.triggers.Trigger;

public class TriggerActionListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		set200Response(c, gson.toJson(JsonAction.convertList(trigger.getActions())));
	}
}
