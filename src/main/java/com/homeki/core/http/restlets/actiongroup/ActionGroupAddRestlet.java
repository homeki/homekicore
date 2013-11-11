package com.homeki.core.http.restlets.actiongroup;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonActionGroup;
import com.homeki.core.main.Util;

public class ActionGroupAddRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		JsonActionGroup jactgrp = getJsonObject(c, JsonActionGroup.class);
		
		if (Util.isNullOrEmpty(jactgrp.name))
			throw new ApiException("Action group name cannot be empty.");
		
		ActionGroup actionGroup = new ActionGroup();
		actionGroup.setName(jactgrp.name);
		actionGroup.setExplicit(true);
		c.ses.save(actionGroup);
		
		JsonActionGroup newid = new JsonActionGroup();
		newid.id = actionGroup.getId();
		
		set200Response(c, gson.toJson(newid));
	}
}
