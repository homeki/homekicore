package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.device.Trigger;

public class JsonTrigger {
	public Integer id;
	public String name;
	public Integer newValue;
	
	public static JsonTrigger[] convertList(List<Trigger> list) {
		JsonTrigger[] array = new JsonTrigger[list.size()];
		
		for (int i = 0; i < array.length; i++) {
			JsonTrigger jt = new JsonTrigger();
			jt.id = list.get(i).getId();
			jt.name = list.get(i).getName();
			jt.newValue = list.get(i).getValue();
			array[i] = jt;
		}
		
		return array;
	}
}
