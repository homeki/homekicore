package com.homeki.core.http.json;

import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.conditions.Condition;
import com.homeki.core.http.ApiException;
import com.homeki.core.main.OperationException;


public class JsonChannelValueCondition extends JsonCondition {
	public Integer deviceId;
	public Integer channel;
	public Number value;
	public String operator;
	
	public JsonChannelValueCondition(ChannelValueCondition cond) {
		super(cond);
		this.deviceId = cond.getDeviceId();
		this.channel = cond.getChannel();
		this.value = cond.getValue();
		this.operator = convertOperator(cond.getOperator());
	}
	
	private String convertOperator(int operator) {
		if (operator == Condition.EQ)
			return "EQ";
		else if (operator == Condition.GT)
			return "GT";
		else if (operator == Condition.LT)
			return "LT";
		else if (operator == Condition.IGNORE)
			return "IGNORE";
		
		throw new OperationException("Tried to convert unknown int operator value to string representation.");
	}
	
	public int getOperatorInt() {
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
