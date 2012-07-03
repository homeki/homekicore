package com.homeki.core.http.handlers;

import com.homeki.core.conditions.ChannelChangedCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonChannelChangedCondition;
import com.homeki.core.http.json.JsonCondition;
import com.homeki.core.triggers.Trigger;

public class TriggerConditionHandler extends HttpHandler {
	public enum Actions {
		LIST, ADD, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) throws Exception {
		c.path.nextToken(); // dismiss "trigger"
		c.path.nextToken(); // dismiss "condition"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception ignore) {}
		
		switch (action) {
		case LIST:
			resolveList(c);
			break;
		case ADD:
			resolveAdd(c);
			break;
		default:
			throw new ApiException("No such URL/action.");
		}
	}

	private void resolveAdd(Container c) {
		String type = getStringParameter(c, "type");
		int triggerId = getIntParameter(c, "triggerId");
		
		Condition condition = null;
		Trigger trigger = (Trigger)c.session.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
			
		if (type.equals("channelchanged"))
			condition = parseChannelChanged(c);
		else
			throw new ApiException("No such condition type.");
		
		c.session.save(condition);
		trigger.setCondition(condition);
		
		JsonCondition newid = new JsonCondition();
		newid.id = condition.getId();
		
		set200Response(c, gson.toJson(newid));
	}
	
	private Condition parseChannelChanged(Container c) {
		String post = getPost(c);
		JsonChannelChangedCondition jcond = gson.fromJson(post, JsonChannelChangedCondition.class);
		
		int op;
		if (jcond.operator.equals("EQ"))
			op = Condition.EQ;
		else if (jcond.operator.equals("GT"))
			op = Condition.GT;
		else if (jcond.operator.equals("LT"))
			op = Condition.LT;
		else
			throw new ApiException("No such operator available. The possible pperators EQ, GT or LT.");
		
		// TODO: add more validation here (does device exist, does device have channel, etc)
			
		return new ChannelChangedCondition(jcond.deviceId, jcond.channel, jcond.number, op);
	}

	private void resolveList(Container c) {
		
	}
}
