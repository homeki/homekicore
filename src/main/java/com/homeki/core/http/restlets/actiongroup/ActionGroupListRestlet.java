package com.homeki.core.http.restlets.actiongroup;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.homeki.core.actions.ActionGroup;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonActionGroup;

public class ActionGroupListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		@SuppressWarnings("unchecked")
		List<ActionGroup> list = c.ses.createCriteria(ActionGroup.class)
				.add(Restrictions.eq("explicit", true))
				.list();
		set200Response(c, gson.toJson(JsonActionGroup.convertList(list)));
	}
}
