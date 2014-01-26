package com.homeki.core.json.conditions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.conditions.SpecialValueCondition;
import com.homeki.core.events.SpecialValueChangedEvent;

@JsonTypeName("specialvalue")
public class JsonSpecialValueCondition extends JsonCondition {
	public String source;
	public Integer value;
	public String operator;
	public Boolean customSource;

	public JsonSpecialValueCondition() {

	}
	
	public JsonSpecialValueCondition(SpecialValueCondition cond) {
		super(cond);
		this.source = cond.getSource();
		this.value = cond.getValue();
		this.operator = convertIntOperator(cond.getOperator());
		this.customSource = SpecialValueChangedEvent.verifySource(cond.getSource());
	}
}
