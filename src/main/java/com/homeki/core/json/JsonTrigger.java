package com.homeki.core.json;

import com.homeki.core.triggers.Trigger;

import java.util.List;

public class JsonTrigger {
	public Integer triggerId;
	public String name;
	public String description;

	public JsonTrigger() {
		
	}
	
	public JsonTrigger(Trigger trigger) {
		this.triggerId = trigger.getId();
		this.name = trigger.getName();
		this.description = trigger.getDescription();
	}
	
	public static JsonTrigger[] convertList(List<Trigger> triggers) {
		JsonTrigger[] jsonTriggers = new JsonTrigger[triggers.size()];
		
		for (int i = 0; i < jsonTriggers.length; i++)
			jsonTriggers[i] = new JsonTrigger(triggers.get(i));
		
		return jsonTriggers;
	}
}
