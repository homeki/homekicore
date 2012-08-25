package com.homeki.core.http.restlets.trigger.condition;

import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.conditions.MinuteCondition;
import com.homeki.core.device.Device;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonChannelValueCondition;
import com.homeki.core.http.json.JsonCondition;
import com.homeki.core.http.json.JsonMinuteCondition;
import com.homeki.core.triggers.Trigger;

public class TriggerConditionAddRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		String type = getStringParam(c, "type");
		
		Condition condition = null;
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
			
		if (type.equals("channelvalue"))
			condition = parseChannelChanged(c);
		else if (type.equals("minute"))
			condition = parseMinuteChanged(c);
		else
			throw new ApiException("No such condition type.");
		
		trigger.addCondition(condition);
		c.ses.save(condition);
		
		JsonCondition newid = new JsonCondition();
		newid.id = condition.getId();
		
		set200Response(c, gson.toJson(newid));
	}
	
	private Condition parseChannelChanged(Container c) {
		JsonChannelValueCondition jcond = getJsonObject(c, JsonChannelValueCondition.class);
		
		if (jcond.operator == null)
			throw new ApiException("Missing operator.");
		if (jcond.value == null)
			throw new ApiException("Missing number.");
		if (jcond.deviceId == null)
			throw new ApiException("Missing deviceId.");
		if (jcond.channel == null)
			throw new ApiException("Missing channel.");
		
		int op = convertOperatorString(jcond.operator);

		Device dev = (Device)c.ses.get(Device.class, jcond.deviceId);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		// TODO: add more validation here (does device have channel, etc)
			
		return new ChannelValueCondition(dev, jcond.channel, jcond.value, op);
	}
	
	private Condition parseMinuteChanged(Container c) {
		JsonMinuteCondition jcond = getJsonObject(c, JsonMinuteCondition.class);
		
		if (jcond.day == null)
			throw new ApiException("Missing day.");
		if (jcond.weekday == null)
			throw new ApiException("Missing weekday.");
		
		// TODO: add more validation here (is everything passed valid, etc)
		
		MinuteCondition cond = new MinuteCondition.Builder()
			.day(jcond.day)
			.hour(jcond.hour)
			.minute(jcond.minute)
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
			throw new ApiException("No such operator available. The possible operators EQ, GT or LT.");
		return op;
	}
}
