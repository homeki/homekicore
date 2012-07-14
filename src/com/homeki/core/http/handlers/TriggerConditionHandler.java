package com.homeki.core.http.handlers;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.conditions.ChannelChangedCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.conditions.MinuteChangedCondition;
import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonChannelChangedCondition;
import com.homeki.core.http.json.JsonCondition;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.http.json.JsonMinuteChangedCondition;
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

	private void resolveList(Container c) {
		int triggerId = getIntParameter(c, "triggerId");
		
		Trigger trigger = (Trigger)c.session.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		List<Condition> list = new ArrayList<Condition>();
		
		if (trigger.getCondition() != null)
			list.add(trigger.getCondition());
		
		set200Response(c, gson.toJson(JsonCondition.convertList(list)));
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
		else if (type.equals("minutechanged"))
			condition = parseMinuteChanged(c);
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
		
		if (jcond.operator == null)
			throw new ApiException("Missing operator.");
		if (jcond.number == null)
			throw new ApiException("Missing number.");
		if (jcond.deviceId == null)
			throw new ApiException("Missing deviceId.");
		if (jcond.channel == null)
			throw new ApiException("Missing channel.");
		
		int op = convertOperatorString(jcond.operator);

		Device dev = (Device)c.session.get(Device.class, jcond.deviceId);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		// TODO: add more validation here (does device have channel, etc)
			
		return new ChannelChangedCondition(dev, jcond.channel, jcond.number, op);
	}
	
	private Condition parseMinuteChanged(Container c) {
		String post = getPost(c);
		JsonMinuteChangedCondition jcond = gson.fromJson(post, JsonMinuteChangedCondition.class);
		
		if (jcond.timeOperator == null)
			throw new ApiException("Missing timeOperator.");
		if (jcond.day == null)
			throw new ApiException("Missing day.");
		if (jcond.weekday == null)
			throw new ApiException("Missing weekday.");
		
		int op = convertOperatorString(jcond.timeOperator);
		
		// TODO: add more validation here (is everything passed valid, etc)
		
		MinuteChangedCondition cond = new MinuteChangedCondition.Builder()
			.day(jcond.day)
			.hour(jcond.hour)
			.minute(jcond.minute)
			.timeOperator(op)
			.weekday(jcond.weekday)
			.build();
		
		return cond;
	}
	
	private int convertOperatorString(String operator) {
		int op;
		if (operator.equals("EQ"))
			op = Condition.EQ;
		else if (operator.equals("GT"))
			op = Condition.GT;
		else if (operator.equals("LT"))
			op = Condition.LT;
		else
			throw new ApiException("No such operator available. The possible pperators EQ, GT or LT.");
		return op;
		
	}
}
