package com.homeki.core.device.tellstick;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;

@Entity
public class TellStickThermometer extends Device {
	public static final int TEMPERATURE_CHANNEL = 0;
	
	public TellStickThermometer() {
		
	}
	
	public TellStickThermometer(double defaultValue) {
		addHistoryPoint(TEMPERATURE_CHANNEL, defaultValue);
	}
	
	@Override
	public String getType() {
		return "thermometer";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(TEMPERATURE_CHANNEL, "temperature", Channel.DOUBLE));
		return list;
	}
}
