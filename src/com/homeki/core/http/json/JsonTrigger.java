package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.triggers.Trigger;

public class JsonTrigger {
	public String name;
	
	public JsonTrigger(Trigger trigger) {
		this.name = trigger.getName();
	}
	
	public static JsonTrigger[] convertList(List<Trigger> triggers) {
		JsonTrigger[] jsonTriggers = new JsonTrigger[triggers.size()];
		
		for (int i = 0; i < jsonTriggers.length; i++)
			jsonTriggers[i] = new JsonTrigger(triggers.get(i));
		
		return jsonTriggers;
	}
}
