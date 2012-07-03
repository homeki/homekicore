package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.conditions.Condition;

public class JsonCondition {
	public String type;
	public Integer id;
	public String shortDescription;
	
	public JsonCondition() {
		
	}
	
	public JsonCondition(Condition condition) {
		this.type = condition.getClass().getSimpleName();
		this.id = condition.getId();
		this.shortDescription = condition.toString();
	}
	
	public static JsonCondition[] convertList(List<Condition> conditions) {
		JsonCondition[] jsonConditions = new JsonCondition[conditions.size()];
		
		for (int i = 0; i < jsonConditions.length; i++)
			jsonConditions[i] = new JsonCondition(conditions.get(i));
		
		return jsonConditions;
	}
}
