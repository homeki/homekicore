package com.homeki.core.events;

public class ChannelValueChangedEvent extends Event {
	public final int deviceId;
	public final int channel;
	public final Number value;
	
	public ChannelValueChangedEvent(int deviceId, int channel, Number value) {
		super();
		this.deviceId = deviceId;
		this.channel = channel;
		this.value = value;
	}
}
