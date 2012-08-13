package com.homeki.core.device.onewire;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DoubleHistoryPoint;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class OneWireThermometer extends OneWireDevice implements OneWireIntervalLoggable {
	private static final int TEMPERATURE_CHANNEL = 0;
	
	public OneWireThermometer() {

	}
	
	public OneWireThermometer(double defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
	@Override
	public void updateValue() throws FileNotFoundException {
		double value = getDoubleVar("temperature");
		addHistoryPoint(value);
	}
	
	public void addHistoryPoint(double value) {
		DoubleHistoryPoint dhp = new DoubleHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		historyPoints.add(dhp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), 0, value));
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
