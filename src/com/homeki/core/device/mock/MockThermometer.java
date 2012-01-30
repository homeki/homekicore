package com.homeki.core.device.mock;

import java.util.Date;
import java.util.Random;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.entities.TemperatureHistoryPoint;

public class MockThermometer extends Device implements IntervalLoggable {
	private Random rnd;
	
	public MockThermometer() {
		rnd = new Random();
	}

	@Override
	public void updateValue() {
		double temp = getRandomThermometerValue();
		TemperatureHistoryPoint thp = new TemperatureHistoryPoint();
		thp.setDevice(this);
		thp.setRegistered(new Date());
		thp.setValue(temp);
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
