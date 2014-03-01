package com.homeki.core.json;

import com.homeki.core.actions.ActionGroup;

import java.util.List;

public class JsonActionGroup {
	public Integer actionGroupId;
	public String name;
	public String description;
	
	public JsonActionGroup() {
		
	}
	
	public JsonActionGroup(ActionGroup actionGroup) {
		this.actionGroupId = actionGroup.getId();
		this.name = actionGroup.getName();
		this.description = actionGroup.getDescription();
	}
	
	public static JsonActionGroup[] convertList(List<ActionGroup> actionGroups) {
		JsonActionGroup[] jsonActionGroups = new JsonActionGroup[actionGroups.size()];
		
		for (int i = 0; i < jsonActionGroups.length; i++)
			jsonActionGroups[i] = new JsonActionGroup(actionGroups.get(i));
		
		return jsonActionGroups;
	}
}
