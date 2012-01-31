package com.homeki.core.device.onewire;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.TemperatureHistoryPoint;

@Entity
public class OneWireThermometer extends OneWireDevice implements OneWireIntervalLoggable {
	@Override
	public void updateValue() {
		double value = getDoubleVar("temperature");
		
		TemperatureHistoryPoint thp = new TemperatureHistoryPoint();
		thp.setDevice(this);
		thp.setRegistered(new Date());
		thp.setValue(value);
		historyPoints.add(thp);
	}

	@Override
	public String getType() {
		return "thermometer";
	}
}
