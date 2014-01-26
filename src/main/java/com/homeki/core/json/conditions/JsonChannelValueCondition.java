package com.homeki.core.json.conditions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.conditions.ChannelValueCondition;

@JsonTypeName("channelvalue")
public class JsonChannelValueCondition extends JsonCondition {
	public Integer deviceId;
	public Integer channel;
	public Number value;
	public String operator;

	public JsonChannelValueCondition() {

	}
	
	public JsonChannelValueCondition(ChannelValueCondition cond) {
		super(cond);
		this.deviceId = cond.getDeviceId();
		this.channel = cond.getChannel();
		this.value = cond.getValue();
		this.operator = convertIntOperator(cond.getOperator());
	}
}
