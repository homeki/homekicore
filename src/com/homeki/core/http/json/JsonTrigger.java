package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.triggers.Trigger;

public class JsonTrigger {
	public Integer id;
	public String name;
	
	public JsonTrigger() {
		
	}
	
	public JsonTrigger(Trigger trigger) {
		this.id = trigger.getId();
		this.name = trigger.getName();
	}
	
	public static JsonTrigger[] convertList(List<Trigger> triggers) {
		JsonTrigger[] jsonTriggers = new JsonTrigger[triggers.size()];
		
		for (int i = 0; i < jsonTriggers.length; i++)
			jsonTriggers[i] = new JsonTrigger(triggers.get(i));
		
		return jsonTriggers;
	}
}
