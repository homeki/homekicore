package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.device.Trigger;

public class JsonTrigger {
	public Integer id;
	public String name;
	public String meta;
	public Integer newValue;
	public String type;
	
	
	public static JsonTrigger[] convertList(List<Trigger> list) {
		JsonTrigger[] array = new JsonTrigger[list.size()];
		
		for (int i = 0; i < array.length; i++) {
			JsonTrigger jt = new JsonTrigger();
			Trigger t = list.get(i);
			jt.id = t.getId();
			jt.name = t.getName();
			jt.newValue = t.getValue();
			jt.meta = t.getMeta();
			jt.type = t.getType();
			
			array[i] = jt;
		}
		
		return array;
	}
}
