package com.homeki.core.http.restlets.trigger;

import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.triggers.Trigger;

public class TriggerDeleteRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		c.ses.delete(trigger);
		
		set200Response(c, "Trigger deleted successfully.");
	}
}
