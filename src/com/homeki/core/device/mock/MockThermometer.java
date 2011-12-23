package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HTemperatureHistoryPoint;

public class MockThermometer extends MockDevice implements IntervalLoggable<Double> {
	private Random rnd;
	
	public MockThermometer(String internalId) {
		super(internalId);
		rnd = new Random();
	}
	
	@Override
	public Double getValue() {
		return getRandomThermometerValue();
	}
	
	@Override
	public void setValue(Double value) {
		Hibernate.putHistoryValue(id, new HTemperatureHistoryPoint(value));
	}

	@Override
	public void updateValue() {
		double temp = getRandomThermometerValue();
		setValue(temp);
	}
	
	private Double getRandomThermometerValue() {
		long sleepTime = 1000 + (rnd.nextInt(500) - 250);
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { }
		
		return (rnd.nextDouble() * 2 - 1) * 40;
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
