package com.homeki.core.conditions;

import com.homeki.core.device.Device;
import com.homeki.core.events.SpecialValueChangedEvent;
import com.homeki.core.http.ApiException;
import com.homeki.core.json.conditions.JsonChannelValueCondition;
import com.homeki.core.json.conditions.JsonCondition;
import com.homeki.core.json.conditions.JsonMinuteCondition;
import com.homeki.core.json.conditions.JsonSpecialValueCondition;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;

public class ConditionParser {
	public static Condition createCondition(JsonCondition jcond) {
		if (jcond instanceof JsonChannelValueCondition)
			return createChannelValueCondition((JsonChannelValueCondition) jcond);
		else if (jcond instanceof JsonMinuteCondition)
			return createMinuteCondition((JsonMinuteCondition) jcond);
		else if (jcond instanceof JsonSpecialValueCondition)
			return createSpecialValueCondition((JsonSpecialValueCondition) jcond);
		else
			throw new ApiException("No such condition type.");
	}

	public static void updateCondition(Condition condition, JsonCondition jcond) {
		if (jcond instanceof JsonChannelValueCondition)
			updateChannelValueCondition((JsonChannelValueCondition)jcond, (ChannelValueCondition)condition);
		else if (jcond instanceof JsonMinuteCondition)
			updateMinuteCondition((JsonMinuteCondition)jcond, (MinuteCondition)condition);
		else if (jcond instanceof JsonSpecialValueCondition)
			updateSpecialValueCondition((JsonSpecialValueCondition)jcond, (SpecialValueCondition)condition);
		else
			throw new ApiException("No such condition type.");
	}

	private static Condition createChannelValueCondition(JsonChannelValueCondition jcond) {
		if (Util.nullOrEmpty(jcond.operator))
			throw new ApiException("Missing operator.");
		if (jcond.value == null)
			throw new ApiException("Missing value.");
		if (jcond.deviceId == null)
			throw new ApiException("Missing deviceId.");
		if (jcond.channel == null)
			throw new ApiException("Missing channel.");

		int op = JsonCondition.convertStringOperator(jcond.operator);
		Device dev = (Device) Hibernate.currentSession().get(Device.class, jcond.deviceId);

		if (dev == null)
			throw new ApiException("Could not load device from device ID.");

		// TODO: add more validation here (is everything passed valid, etc)

		return new ChannelValueCondition(dev, jcond.channel, jcond.value, op);
	}

	private static Condition createSpecialValueCondition(JsonSpecialValueCondition jcond) {
		if (Util.nullOrEmpty(jcond.operator))
			throw new ApiException("Missing operator.");
		if (Util.nullOrEmpty(jcond.source))
			throw new ApiException("Missing source.");
		if (jcond.value == null)
			throw new ApiException("Missing value.");

		int op = JsonCondition.convertStringOperator(jcond.operator);
		jcond.source = jcond.source.toUpperCase();

		if ((jcond.customSource == null || (jcond.customSource != null && !jcond.customSource)) && !SpecialValueChangedEvent.verifySource(jcond.source))
			throw new ApiException("No source '" + jcond.source + "' exists.");

		// TODO: add more validation here (is everything passed valid, etc)

		return new SpecialValueCondition(jcond.source, jcond.value, op);
	}

	private static Condition createMinuteCondition(JsonMinuteCondition jcond) {
		if (Util.nullOrEmpty(jcond.day))
			throw new ApiException("Missing day.");
		if (Util.nullOrEmpty(jcond.weekday))
			throw new ApiException("Missing weekday.");
		if (jcond.hour == null)
			throw new ApiException("Missing hour.");
		if (jcond.minute == null)
			throw new ApiException("Missing minute.");

		// TODO: add more validation here (is everything passed valid, etc)

		return new MinuteCondition(jcond.day, jcond.weekday, jcond.hour, jcond.minute);
	}

	private static void updateChannelValueCondition(JsonChannelValueCondition jcond, ChannelValueCondition cond) {
		if (!Util.nullOrEmpty(jcond.operator)) {
			cond.setOperator(JsonCondition.convertStringOperator(jcond.operator));
		}
		if (jcond.value != null) {
			cond.setValue(jcond.value);
		}
		if (jcond.deviceId != null) {
			Device dev = (Device)Hibernate.currentSession().get(Device.class, jcond.deviceId);

			if (dev == null)
				throw new ApiException("Could not load new device from device ID.");

			cond.setDevice(dev);
		}
		if (jcond.channel != null) {
			cond.setChannel(jcond.channel);
		}

		// TODO: add more validation here (is everything passed valid, etc)
	}

	private static void updateMinuteCondition(JsonMinuteCondition jcond, MinuteCondition cond) {
		if (!Util.nullOrEmpty(jcond.day))
			cond.setDay(jcond.day);
		if (!Util.nullOrEmpty(jcond.weekday))
			cond.setWeekday(jcond.weekday);
		if (jcond.hour != null)
			cond.setHour(jcond.hour);
		if (jcond.minute != null)
			cond.setMinute(jcond.minute);

		// TODO: add more validation here (is everything passed valid, etc)
	}

	private static void updateSpecialValueCondition(JsonSpecialValueCondition jcond, SpecialValueCondition cond) {
		if (!Util.nullOrEmpty(jcond.operator)) {
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
