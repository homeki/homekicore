package com.homeki.core.http.restlets.trigger;

import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonTrigger;
import com.homeki.core.main.Util;
import com.homeki.core.triggers.Trigger;

public class TriggerAddRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		JsonTrigger jtrigger = getJsonObject(c, JsonTrigger.class);
		
		if (Util.isNullOrEmpty(jtrigger.name))
			throw new ApiException("Trigger name cannot be empty.");
		
		Trigger trigger = new Trigger();
		trigger.setName(jtrigger.name);
		c.ses.save(trigger);
		
		JsonTrigger newid = new JsonTrigger();
		newid.id = trigger.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
