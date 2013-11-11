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

public class TriggerConditionSetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int triggerId = getInt(c, "triggerid");
		int conditionId = getInt(c, "conditionid");
		
		Trigger trigger = (Trigger)c.ses.get(Trigger.class, triggerId);
		
		if (trigger == null)
			throw new ApiException("No trigger with the specified ID found.");
		
		Condition cond = (Condition)c.ses.get(Condition.class, conditionId);
		
		if (cond == null)
			throw new ApiException("No condition with the specified ID found.");
		
		if (cond instanceof ChannelValueCondition)
			parseChannelValueCondition(c, (ChannelValueCondition)cond);
		else if (cond instanceof MinuteCondition)
			parseMinuteCondition(c, (MinuteCondition)cond);
		else if (cond instanceof SpecialValueCondition)
			parseSpecialValueCondition(c, (SpecialValueCondition)cond);
		else
			throw new ApiException("Condition does not yet support /set.");
		
		c.ses.save(cond);
		set200Response(c, msg("Condition updated successfully."));
	}
	
	private void parseChannelValueCondition(Container c, ChannelValueCondition cond) {
		JsonChannelValueCondition jcond = getJsonObject(c, JsonChannelValueCondition.class);
		
		if (!Util.isNullOrEmpty(jcond.operator)) {
			cond.setOperator(JsonCondition.convertStringOperator(jcond.operator));
		}
		if (jcond.value != null) {
			cond.setValue(jcond.value);
		}
		if (jcond.deviceId != null) {
			Device dev = (Device)c.ses.get(Device.class, jcond.deviceId);
			
			if (dev == null)
				throw new ApiException("Could not load new device from device ID.");
			
			cond.setDevice(dev);
		}
		if (jcond.channel != null) {
			cond.setChannel(jcond.channel);
		}
		
		// TODO: add more validation here (is everything passed valid, etc)
	}
	
	private void parseMinuteCondition(Container c, MinuteCondition cond) {
		JsonMinuteCondition jcond = getJsonObject(c, JsonMinuteCondition.class);
		
		if (!Util.isNullOrEmpty(jcond.day))
			cond.setDay(jcond.day);
		if (!Util.isNullOrEmpty(jcond.weekday))
			cond.setWeekday(jcond.weekday);
		if (jcond.hour != null)
			cond.setHour(jcond.hour);
		if (jcond.minute != null)
			cond.setMinute(jcond.minute);
		
		// TODO: add more validation here (is everything passed valid, etc)
	}
	
	private void parseSpecialValueCondition(Container c, SpecialValueCondition cond) {
		JsonSpecialValueCondition jcond = getJsonObject(c, JsonSpecialValueCondition.class);
		
		if (!Util.isNullOrEmpty(jcond.operator)) {
			cond.setOperator(JsonCondition.convertStringOperator(jcond.operator));
		}
		if (jcond.value != null) {
			cond.setValue(jcond.value);
		}
		if (jcond.source != null) {
			jcond.source = jcond.source.toUpperCase();
			
			if (!SpecialValueChangedEvent.verifySource(jcond.source))
				throw new ApiException("No source '" + jcond.source + "' exists.");
			
			cond.setSource(jcond.source);
		}
		
		// TODO: add more validation here (is everything passed valid, etc)
	}
}
