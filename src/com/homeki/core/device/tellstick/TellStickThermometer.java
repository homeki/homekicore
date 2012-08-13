package com.homeki.core.device.tellstick;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.DoubleHistoryPoint;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class TellStickThermometer extends Device {
	private static final int TEMPERATURE_CHANNEL = 0;
	
	public TellStickThermometer() {
		
	}
	
	public TellStickThermometer(double defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
	@Override
	public String getType() {
		return "thermometer";
	}
	
	public void addHistoryPoint(double value) {
		DoubleHistoryPoint thp = new DoubleHistoryPoint();
		thp.setDevice(this);
		thp.setRegistered(new Date());
		thp.setValue(value);
		historyPoints.add(thp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), 0, value));
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(TEMPERATURE_CHANNEL, "temperature", Channel.DOUBLE));
		return list;
	}
}
