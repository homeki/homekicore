package com.homeki.core.events;

import javax.persistence.Column;
import javax.persistence.Entity;

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
	
	public boolean check(Event e) {
		if (e instanceof ChannelChangedEvent) {
			ChannelChangedEvent cce = (ChannelChangedEvent) e;
			if (cce.channel == this.getChannel() && cce.deviceId == this.getDeviceId()) {
				int v = Double.compare(cce.value.doubleValue(), getValue().doubleValue());
				switch (getOperator()) {
				case EQ:
					return v == 0;
				case LT:
					return v < 0;
				case GT:
					return v > 0;
				}
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
}
