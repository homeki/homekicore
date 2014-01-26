package com.homeki.core.http.json;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.actions.TriggerActionGroupAction;

@JsonTypeName("triggeractiongroup")
public class JsonTriggerActionGroupAction extends JsonAction {
	public Integer actionGroupId;

	public JsonTriggerActionGroupAction() {

	}
	
	public JsonTriggerActionGroupAction(TriggerActionGroupAction action) {
		super(action);
		this.actionGroupId = action.getActionGroupId();
	}
}
