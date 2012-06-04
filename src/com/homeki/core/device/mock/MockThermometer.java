package com.homeki.core.device.mock;

import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.homeki.core.device.Device;
import com.homeki.core.device.DoubleHistoryPoint;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class MockThermometer extends Device {
	@Transient
	private Random rnd;
	
	public MockThermometer() {
		rnd = new Random();
	}
	
	public MockThermometer(double defaultValue) {
		this();
		addHistoryPoint(defaultValue);
	}

	// TODO: log this using a thread or something, like in onewire
	public void storeNewValue() {
		double temp = getRandomThermometerValue();
		addHistoryPoint(temp);
	}
	
	private Double getRandomThermometerValue() {
		long sleepTime = 1000 + (rnd.nextInt(500) - 250);
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { }
		
		return (rnd.nextDouble() * 2 - 1) * 40;
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
}
