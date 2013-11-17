package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.actions.Action;
import com.homeki.core.actions.ChangeChannelValueAction;
import com.homeki.core.actions.SendMailAction;
import com.homeki.core.actions.TriggerActionGroupAction;
import com.homeki.core.main.OperationException;

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
		
		throw new OperationException("Tried to create JSON action from unknown action.");
	}
}