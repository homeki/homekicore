package com.homeki.core.http.restlets.actiongroup.action;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;

public class ActionGroupActionDeleteRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int actionGroupId = getInt(c, "actiongroupid");
		int actionId = getInt(c, "actionid");
		
		ActionGroup actionGroup = (ActionGroup)c.ses.get(ActionGroup.class, actionGroupId);
		
		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");
		
		Action act = (Action)c.ses.get(Action.class, actionId);
		
		if (act == null)
			throw new ApiException("No action with the specified ID found.");
		
		actionGroup.deleteAction(act);
		
		set200Response(c, msg("Action successfully deleted."));
	}
}
