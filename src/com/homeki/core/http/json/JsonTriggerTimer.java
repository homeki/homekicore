package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.triggers.Trigger;

public class JsonTriggerTimer {
	public Integer id;
	public String name;
	public Integer time;
	public Integer repeatType;
	public Integer days;
	
	public JsonTriggerTimer(Trigger t) {
//		type = t.getType();
		id = t.getId();
		name = t.getName();
		time = t.getValue();
		repeatType = t.getRepeatType();
		days = t.getDays();
	}

	public static JsonTriggerTimer[] convertList(List<Trigger> triggers) {
		JsonTriggerTimer[] jsonTimers = new JsonTriggerTimer[triggers.size()];
		for (int i = 0; i < jsonTimers.length; i++)
			jsonTimers[i] = new JsonTriggerTimer(triggers.get(i));
		return jsonTimers;
	}
}
