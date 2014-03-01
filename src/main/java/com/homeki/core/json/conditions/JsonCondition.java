package com.homeki.core.json.conditions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.conditions.MinuteCondition;
import com.homeki.core.conditions.SpecialValueCondition;
import com.homeki.core.http.ApiException;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
								@JsonSubTypes.Type(value = JsonMinuteCondition.class, name = "minute"),
								@JsonSubTypes.Type(value = JsonChannelValueCondition.class, name = "channelvalue"),
								@JsonSubTypes.Type(value = JsonSpecialValueCondition.class, name = "specialvalue")
})
public class JsonCondition {
	public String type;
	public Integer conditionId;
	
	public JsonCondition() {
		
	}
	
	public JsonCondition(Condition condition) {
		this.conditionId = condition.getId();
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
		else if (cond instanceof SpecialValueCondition)
			return new JsonSpecialValueCondition((SpecialValueCondition)cond);
		
		throw new ApiException("Tried to create JSON condition from unknown condition.");
	}
	
	protected static String convertIntOperator(int operator) {
		if (operator == Condition.EQ)
			return "EQ";
		else if (operator == Condition.GT)
			return "GT";
		else if (operator == Condition.LT)
			return "LT";
		else if (operator == Condition.IGNORE)
			return "IGNORE";
		
		throw new ApiException("Tried to convert unknown int operator value to string representation.");
	}
	
	public static int convertStringOperator(String operator) {
		int op;
		if (operator.equals("EQ"))
			op = Condition.EQ;
		else if (operator.equals("GT"))
			op = Condition.GT;
		else if (operator.equals("LT"))
			op = Condition.LT;
		else
			throw new ApiException("No such operator available. The possible operators EQ, GT or LT.");
		return op;
	}
}
