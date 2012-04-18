package com.homeki.core.events;

public class ChannelChangedEvent extends Event {
	public int deviceId;
	public int channel;
	public Number value;
}
