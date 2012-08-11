package com.homeki.core.http.restlets.trigger.condition;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.conditions.Condition;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonCondition;
import com.homeki.core.triggers.Trigger;

public class TriggerConditionListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		List<Condition> list = new ArrayList<Condition>();
		
		if (trigger.getCondition() != null)
			list.add(trigger.getCondition());
		
		set200Response(c, gson.toJson(JsonCondition.convertList(list)));
	}
}
