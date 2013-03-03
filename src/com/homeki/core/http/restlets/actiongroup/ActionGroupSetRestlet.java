package com.homeki.core.http.restlets.actiongroup;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonActionGroup;
import com.homeki.core.main.Util;

public class ActionGroupSetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int actionGroupId = getInt(c, "actiongroupid");
		
		ActionGroup actgrp = (ActionGroup)c.ses.get(ActionGroup.class, actionGroupId);
		
		if (actgrp == null || !actgrp.isExplicit())
			throw new ApiException("No action group with the specified ID found.");
		
		JsonActionGroup jactgr = getJsonObject(c, JsonActionGroup.class);
		
		if (Util.isNotNullAndEmpty(jactgr.name))
			throw new ApiException("New action group name cannot be empty.");
		
		if (jactgr.name != null)
			actgrp.setName(jactgr.name);
		
		set200Response(c, msg("Action group updated successfully."));
	}
}
