package com.homeki.core.http.restlets.trigger.action;

import com.homeki.core.actions.Action;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.actions.JsonAction;
import com.homeki.core.triggers.Trigger;

public class TriggerActionAddRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		String type = getStringParam(c, "type");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
			
		//Action action = ActionParser.createAction(type, getPost(c));
		Action action = null;
		
		trigger.addAction(action);
		c.ses.save(action);
		
		JsonAction newid = new JsonAction();
		newid.id = action.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
