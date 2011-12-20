package com.homeki.core.device.mock;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HDevice;
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
	public void updateValue() {
		float temp = getRandomThermometerValue();
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		HTemperatureHistoryPoint value = new HTemperatureHistoryPoint();
		value.setRegistered(new Date());
		value.setDevice(dev);
		value.setValue(temp);
		session.save(value);
		Hibernate.closeSession(session);
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
		Session session = Hibernate.openSession();
		@SuppressWarnings("unchecked")
		List<HistoryPoint> list = session.createQuery("from HTemperatureHistoryPoint as p where p.registered between ? and ? order by p.registered asc")
				.setDate(0, from)
				.setDate(1, to)
				.list();
		Hibernate.closeSession(session);
		return list;
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
