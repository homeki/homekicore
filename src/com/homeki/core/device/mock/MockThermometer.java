package com.homeki.core.device.mock;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.homeki.core.device.IntervalLoggable;
import com.homeki.core.storage.DatumPoint;

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
	public void updateValue() {
		float value = getRandomThermometerValue();
		//historyTable.putValue(new Date(), value);
	}
	
	private Float getRandomThermometerValue() {
		long sleepTime = 1000 + (rnd.nextInt(500) - 250);
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { }
		
		return (rnd.nextFloat() * 2 - 1) * 40;
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		//return historyTable.getValues(from, to);
		return null;
	}

	@Override
	protected Type getTableValueType() {
		return Float.class;
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
