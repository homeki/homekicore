package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.actions.ActionGroup;

public class JsonActionGroup {
	public Integer id;
	public String name;
	
	public JsonActionGroup() {
		
	}
	
	public JsonActionGroup(ActionGroup actionGroup) {
		this.id = actionGroup.getId();
		this.name = actionGroup.getName();
	}
	
	public static JsonActionGroup[] convertList(List<ActionGroup> actionGroups) {
		JsonActionGroup[] jsonActionGroups = new JsonActionGroup[actionGroups.size()];
		
		for (int i = 0; i < jsonActionGroups.length; i++)
			jsonActionGroups[i] = new JsonActionGroup(actionGroups.get(i));
		
		return jsonActionGroups;
	}
}
