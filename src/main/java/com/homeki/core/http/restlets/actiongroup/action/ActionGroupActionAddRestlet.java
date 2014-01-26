package com.homeki.core.http.restlets.actiongroup.action;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.actions.JsonAction;

public class ActionGroupActionAddRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int actionGroupId = getInt(c, "actiongroupid");
		String type = getStringParam(c, "type");
		
		ActionGroup actionGroup = (ActionGroup)c.ses.get(ActionGroup.class, actionGroupId);
		
		if (actionGroup == null || !actionGroup.isExplicit())
			throw new ApiException("No action group with the specified ID found.");
			
		//Action action = ActionParser.createAction(type, getPost(c));
		Action action = null;
		
		actionGroup.addAction(action);
		c.ses.save(action);
		
		JsonAction newid = new JsonAction();
		newid.id = action.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
