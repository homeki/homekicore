package com.homeki.core.events;

public class ChannelChangedEvent extends Event {
	public ChannelChangedEvent(int deviceId, int channel, Number value) {
		super();
		this.deviceId = deviceId;
		this.channel = channel;
		this.value = value;
	}
	
	public final int deviceId;
	public final int channel;
	public final Number value;
}
