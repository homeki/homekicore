package com.homeki.core.http.restlets.trigger.condition;

import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.conditions.MinuteCondition;
import com.homeki.core.conditions.SpecialValueCondition;
import com.homeki.core.device.Device;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonChannelValueCondition;
import com.homeki.core.http.json.JsonCondition;
import com.homeki.core.http.json.JsonMinuteCondition;
import com.homeki.core.http.json.JsonSpecialValueCondition;
import com.homeki.core.main.Util;
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
			condition = parseChannelValueCondition(c);
		else if (type.equals("minute"))
			condition = parseMinuteCondition(c);
		else if (type.equals("specialvalue"))
			condition = parseSpecialValueCondition(c);
		else
			throw new ApiException("No such condition type.");
		
		trigger.addCondition(condition);
		c.ses.save(condition);
		
		JsonCondition newid = new JsonCondition();
		newid.id = condition.getId();
		
		set200Response(c, gson.toJson(newid));
	}
	
	private Condition parseChannelValueCondition(Container c) {
		JsonChannelValueCondition jcond = getJsonObject(c, JsonChannelValueCondition.class);
		
		if (Util.isNullOrEmpty(jcond.operator))
			throw new ApiException("Missing operator.");
		if (jcond.value == null)
			throw new ApiException("Missing value.");
		if (jcond.deviceId == null)
			throw new ApiException("Missing deviceId.");
		if (jcond.channel == null)
			throw new ApiException("Missing channel.");
		
		int op = JsonCondition.convertStringOperator(jcond.operator);
		Device dev = (Device)c.ses.get(Device.class, jcond.deviceId);
		
		if (dev == null)
			throw new ApiException("Could not load device from device ID.");
		
		// TODO: add more validation here (is everything passed valid, etc)
			
		return new ChannelValueCondition(dev, jcond.channel, jcond.value, op);
	}
	
	private Condition parseSpecialValueCondition(Container c) {
		JsonSpecialValueCondition jcond = getJsonObject(c, JsonSpecialValueCondition.class);
		
		if (Util.isNullOrEmpty(jcond.operator))
			throw new ApiException("Missing operator.");
		if (Util.isNullOrEmpty(jcond.source))
			throw new ApiException("Missing operator.");
		if (jcond.value == null)
			throw new ApiException("Missing value.");
		
		int op = JsonCondition.convertStringOperator(jcond.operator);
		jcond.source = jcond.source.toUpperCase();
		
		if (!SpecialValueChangedEvent.verifySource(jcond.source))
			throw new ApiException("No source '" + jcond.source + "' exists.");
		
		// TODO: add more validation here (is everything passed valid, etc)
		
		return new SpecialValueCondition(jcond.source, jcond.value, op);
	}
	
	private Condition parseMinuteCondition(Container c) {
		JsonMinuteCondition jcond = getJsonObject(c, JsonMinuteCondition.class);
		
		if (Util.isNullOrEmpty(jcond.day))
			throw new ApiException("Missing day.");
		if (Util.isNullOrEmpty(jcond.weekday))
			throw new ApiException("Missing weekday.");
		if (jcond.hour == null)
			throw new ApiException("Missing hour.");
		if (jcond.minute == null)
			throw new ApiException("Missing minute.");
		
		// TODO: add more validation here (is everything passed valid, etc)
		
		return new MinuteCondition(jcond.day, jcond.weekday, jcond.hour, jcond.minute);
	}
}
