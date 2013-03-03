package com.homeki.core.http.restlets.actiongroup.action;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonAction;

public class ActionGroupActionListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int actionGroupId = getInt(c, "actiongroupid");
		
		ActionGroup actionGroup = (ActionGroup)c.ses.get(ActionGroup.class, actionGroupId);
		
		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");
		
		set200Response(c, gson.toJson(JsonAction.convertList(actionGroup.getActions())));
	}
}
