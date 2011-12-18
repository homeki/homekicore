package com.homeki.core.device.onewire;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.HDevice;
import com.homeki.core.storage.entities.HTemperatureHistory;

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
		HTemperatureHistory value = new HTemperatureHistory();
		value.setRegistered(new Date());
		value.setDevice(dev);
		value.setValue(temp);
		session.save(value);
		Hibernate.closeSession(session);
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return null;
		//return historyTable.getValues(from, to);
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
