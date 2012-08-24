package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.actions.Action;

public class JsonAction {
	public String type;
	public Integer id;
	
	public JsonAction() {
		
	}
	
	public JsonAction(Action action) {
		this.id = action.getId();
		this.type = action.getType();
	}
	
	public static JsonAction[] convertList(List<Action> actions) {
		JsonAction[] jsonActions = new JsonAction[actions.size()];
		
		for (int i = 0; i < jsonActions.length; i++)
			jsonActions[i] = new JsonAction(actions.get(i));
		
		return jsonActions;
	}
}
