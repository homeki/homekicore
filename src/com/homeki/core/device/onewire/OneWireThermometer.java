package com.homeki.core.device.onewire;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;
import com.homeki.core.storage.entities.HDevice;
import com.homeki.core.storage.entities.HTemperatureHistoryPoint;

public class OneWireThermometer extends OneWireDevice implements IntervalLoggable<Float> {
	public OneWireThermometer(String internalId, String deviceDirPath) {
		super(internalId, deviceDirPath);
	}

	@Override
	public Float getValue() {
		return getFloatVar("Thermometer");
	}

	@Override
	public void updateValue() {
		float temp = getFloatVar("Thermometer");
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		HTemperatureHistoryPoint value = new HTemperatureHistoryPoint();
		value.setRegistered(new Date());
		value.setDevice(dev);
		value.setValue(temp);
		session.save(value);
		Hibernate.closeSession(session);
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
