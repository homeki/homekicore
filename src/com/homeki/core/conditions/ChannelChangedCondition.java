package com.homeki.core.conditions;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.Event;

@Entity
public class ChannelChangedCondition extends Condition {
	@Column
	private int deviceId;
	
	@Column
	private int channel;
	
	@Column
	private Number value;
	
	@Column
	private int operator;
	
	protected ChannelChangedCondition() {}
	
	public ChannelChangedCondition(int deviceId, int channel, Number value, int operator) {
		this.deviceId = deviceId;
		this.channel = channel;
		this.value = value;
		this.operator = operator;
	}
	
	public boolean check(Event e) {
		if (e instanceof ChannelChangedEvent) {
			ChannelChangedEvent cce = (ChannelChangedEvent) e;
			if (cce.channel == this.getChannel() && cce.deviceId == this.getDeviceId()) {
				return evalute(cce.value, getValue(), getOperator());
			}
		}
		return false;
	}
	
	public int getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	
	public int getChannel() {
		return channel;
	}
	
	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	public Number getValue() {
		return value;
	}
	
	public void setValue(Number value) {
		this.value = value;
	}
	
	public int getOperator() {
		return operator;
	}
	
	public void setOperator(int operator) {
		this.operator = operator;
	}
	
	@Override
	public String toString() {
		return channel + " on " + deviceId + " == " + value;
	}
}
