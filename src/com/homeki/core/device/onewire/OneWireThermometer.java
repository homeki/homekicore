package com.homeki.core.device.onewire;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.TemperatureHistoryPoint;

@Entity
public class OneWireThermometer extends OneWireDevice implements OneWireIntervalLoggable {
	public OneWireThermometer() {

	}
	
	public OneWireThermometer(double defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
	@Override
	public void updateValue() {
		double value = getDoubleVar("temperature");
		addHistoryPoint(value);
	}
	
	public void addHistoryPoint(double value) {
		TemperatureHistoryPoint dhp = new TemperatureHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		historyPoints.add(dhp);
	}
	
	@Override
	public String getType() {
		return "thermometer";
	}
}
