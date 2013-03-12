package com.homeki.core.http.json;

import com.homeki.core.conditions.SpecialValueCondition;


public class JsonSpecialValueCondition extends JsonCondition {
	public String source;
	public Integer value;
	public String operator;
	
	public JsonSpecialValueCondition(SpecialValueCondition cond) {
		super(cond);
		this.source = cond.getSource();
		this.value = cond.getValue();
		this.operator = convertIntOperator(cond.getOperator());
	}
}
