package com.homeki.core.http.restlets.actiongroup.action;

import com.homeki.core.actions.Action;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.triggers.Trigger;

public class ActionGroupActionSetRestlet extends KiRestlet {
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
		
		//ActionParser.updateAction(act, getPost(c));
		
		c.ses.save(act);
		set200Response(c, msg("Action updated successfully."));
	}
}
