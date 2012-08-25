package com.homeki.core.conditions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.homeki.core.device.Device;
import com.homeki.core.events.ChannelChangedEvent;
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
		if (e instanceof ChannelChangedEvent) {
			ChannelChangedEvent cce = (ChannelChangedEvent) e;
			if (cce.channel == channel && cce.deviceId == device.getId())
				status = evalute(cce.value, value, operator);
		}
		return status;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public void setDevice(Device device) {
		this.device = device;
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
	public String getType() {
		return "channelvalue";
	}
}
