package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HTemperatureHistoryPoint;

public class MockThermometer extends MockDevice implements IntervalLoggable<Float> {
	private Random rnd;
	
	public MockThermometer(String internalId) {
		super(internalId);
		rnd = new Random();
	}
	
	@Override
	public Float getValue() {
		return getRandomThermometerValue();
	}
	
	@Override
	public void setValue(Float value) {
		Hibernate.putHistoryValue(id, new HTemperatureHistoryPoint(value));
	}

	@Override
	public void updateValue() {
		float temp = getRandomThermometerValue();
		setValue(temp);
	}
	
	private Float getRandomThermometerValue() {
		long sleepTime = 1000 + (rnd.nextInt(500) - 250);
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { }
		
		return (rnd.nextFloat() * 2 - 1) * 40;
	}

	@Override
	public List<HistoryPoint> getHistory(Date from, Date to) {
		return Hibernate.getTemperatureHistoryPoints(from, to);
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
