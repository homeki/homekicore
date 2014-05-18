package com.homeki.core.device.tellstick;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;
import com.homeki.core.device.Device;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

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
		List<Channel> list = new ArrayList<>();
		list.add(new Channel(TEMPERATURE_CHANNEL, "temperature", DataType.DOUBLE));
		return list;
	}
}
