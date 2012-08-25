package com.homeki.core.conditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.homeki.core.device.Device;
import com.homeki.core.events.ChannelValueChangedEvent;
import com.homeki.core.events.Event;

@Entity
public class ChannelValueCondition extends Condition {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private Device device;
	
	@Column
	private int channel;
	
	@Column
	private Number value;
	
	@Column
	private int operator;
	
	public ChannelValueCondition() {
		
	}
	
	public ChannelValueCondition(Device device, int channel, Number value, int operator) {
		this.device = device;
		this.channel = channel;
		this.value = value;
		this.operator = operator;
	}
	
	public boolean check(Event e) {
		if (e instanceof ChannelValueChangedEvent) {
			ChannelValueChangedEvent cce = (ChannelValueChangedEvent) e;
			if (cce.channel == channel && cce.deviceId == device.getId())
				status = evalute(cce.value, value, operator);
		}
		return status;
	}
	
	public int getDeviceId() {
		return device.getId();
	}
	
	public int getChannel() {
		return channel;
	}
	
	public Number getValue() {
		return value;
	}
	
	public int getOperator() {
		return operator;
	}
	
	@Override
	public String getType() {
		return "channelvalue";
	}
}
