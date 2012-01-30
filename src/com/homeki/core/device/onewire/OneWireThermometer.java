package com.homeki.core.device.onewire;

import java.util.Date;

import com.homeki.core.storage.entities.TemperatureHistoryPoint;


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
