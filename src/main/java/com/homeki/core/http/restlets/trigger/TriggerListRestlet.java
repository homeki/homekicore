package com.homeki.core.http.restlets.trigger;

import java.util.List;

import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonTrigger;
import com.homeki.core.triggers.Trigger;

public class TriggerListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		@SuppressWarnings("unchecked")
		List<Trigger> list = c.ses.createCriteria(Trigger.class).list();
		set200Response(c, gson.toJson(JsonTrigger.convertList(list)));
	}
}
