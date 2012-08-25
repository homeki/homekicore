package com.homeki.core.http.json;

import java.util.List;

import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.conditions.MinuteCondition;
import com.homeki.core.main.OperationException;

public class JsonCondition {
	public String type;
	public Integer id;
	
	public JsonCondition() {
		
	}
	
	public JsonCondition(Condition condition) {
		this.id = condition.getId();
		this.type = condition.getType();
	}
	
	public static JsonCondition[] convertList(List<Condition> conditions) {
		JsonCondition[] jsonConditions = new JsonCondition[conditions.size()];
		
		for (int i = 0; i < jsonConditions.length; i++)
			jsonConditions[i] = JsonCondition.create(conditions.get(i));
		
		return jsonConditions;
	}

	public static JsonCondition create(Condition cond) {
		if (cond instanceof MinuteCondition)
			return new JsonMinuteCondition((MinuteCondition)cond);
		else if (cond instanceof ChannelValueCondition)
			return new JsonChannelValueCondition((ChannelValueCondition)cond);
		
		throw new OperationException("Tried to create JSON condition from unknown condition.");
	}
}
