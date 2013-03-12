package com.homeki.core.http.json;

import com.homeki.core.conditions.ChannelValueCondition;


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
		this.operator = convertIntOperator(cond.getOperator());
	}
}
