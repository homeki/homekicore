package com.homeki.core.http.json;

import com.homeki.core.actions.TriggerActionGroupAction;

public class JsonTriggerActionGroupAction extends JsonAction {
	public Integer actionGroupId;
	
	public JsonTriggerActionGroupAction(TriggerActionGroupAction action) {
		super(action);
		this.actionGroupId = action.getActionGroupId();
	}
}
