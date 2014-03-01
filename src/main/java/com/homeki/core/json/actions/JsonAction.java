package com.homeki.core.json.actions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.homeki.core.actions.Action;
import com.homeki.core.actions.ChangeChannelValueAction;
import com.homeki.core.actions.SendMailAction;
import com.homeki.core.actions.TriggerActionGroupAction;
import com.homeki.core.http.ApiException;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
								@JsonSubTypes.Type(value = JsonChangeChannelValueAction.class, name = "changechannelvalue"),
								@JsonSubTypes.Type(value = JsonTriggerActionGroupAction.class, name = "triggeractiongroup"),
								@JsonSubTypes.Type(value = JsonSendMailAction.class, name = "sendmail")
})
public class JsonAction {
	public String type;
	public Integer actionId;
	
	public JsonAction() {
		
	}
	
	public JsonAction(Action action) {
		this.actionId = action.getId();
		this.type = action.getType();
	}
	
	public static JsonAction[] convertList(List<Action> actions) {
		JsonAction[] jsonActions = new JsonAction[actions.size()];
		
		for (int i = 0; i < jsonActions.length; i++)
			jsonActions[i] = JsonAction.create(actions.get(i));
		
		return jsonActions;
	}

	public static JsonAction create(Action act) {
		if (act instanceof ChangeChannelValueAction)
			return new JsonChangeChannelValueAction((ChangeChannelValueAction)act);
		else if (act instanceof TriggerActionGroupAction)
			return new JsonTriggerActionGroupAction((TriggerActionGroupAction)act);
		else if (act instanceof SendMailAction)
			return new JsonSendMailAction((SendMailAction)act);
		
		throw new ApiException("Tried to create JSON action from unknown action.");
	}
}
