package com.homeki.core.device.mock;

import java.util.Date;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.homeki.core.device.Device;
import com.homeki.core.device.onewire.OneWireIntervalLoggable;
import com.homeki.core.storage.entities.TemperatureHistoryPoint;

@Entity
public class MockThermometer extends Device {
	@Transient
	private Random rnd;
	
	public MockThermometer() {
		rnd = new Random();
	}

	// TODO: log this using a thread or something, like in onewire
	public void storeNewValue() {
		double temp = getRandomThermometerValue();
		TemperatureHistoryPoint thp = new TemperatureHistoryPoint();
		thp.setDevice(this);
		thp.setRegistered(new Date());
		thp.setValue(temp);
		historyPoints.add(thp);
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
}
