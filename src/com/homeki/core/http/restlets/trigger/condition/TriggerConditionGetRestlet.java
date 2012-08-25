package com.homeki.core.http.restlets.trigger.condition;

import com.homeki.core.conditions.Condition;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonCondition;
import com.homeki.core.triggers.Trigger;

public class TriggerConditionGetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		int conditionId = getInt(c, "conditionid");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		Condition cond = (Condition)c.ses.get(Condition.class, conditionId);
		
		if (cond == null)
			throw new ApiException("No condition with the specified ID found.");
		
		set200Response(c, gson.toJson(JsonCondition.create(cond)));
	}
}
