package com.homeki.core.http.restlets.actiongroup;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;

public class ActionGroupDeleteRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int actionGroupId = getInt(c, "actiongroupid");
		
		ActionGroup actionGroup = (ActionGroup)c.ses.get(ActionGroup.class, actionGroupId);
		
		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");
		
		// TODO: add checks for future connected actions which possibly trigger this actiongroup before deletion
		
		c.ses.delete(actionGroup);
		
		set200Response(c, msg("Action group deleted successfully."));
	}
}
