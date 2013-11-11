package com.homeki.core.device.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;

@Entity
public class MockThermometer extends Device {
	private static final int TEMPERATURE_CHANNEL = 0;
	
	@Transient
	private Random rnd;
	
	public MockThermometer() {
		rnd = new Random();
	}
	
	public MockThermometer(double defaultValue) {
		this();
		addHistoryPoint(TEMPERATURE_CHANNEL, defaultValue);
	}

	// TODO: log this using a thread or something, like in onewire
	public void storeNewValue() {
		double temp = getRandomThermometerValue();
		addHistoryPoint(TEMPERATURE_CHANNEL, temp);
	}
	
	private Double getRandomThermometerValue() {
		long sleepTime = 1000 + (rnd.nextInt(500) - 250);
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { }
		
		return (rnd.nextDouble() * 2 - 1) * 40;
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
