package com.homeki.core.device.onewire;

import java.io.FileNotFoundException;
import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.DoubleHistoryPoint;

@Entity
public class OneWireThermometer extends OneWireDevice implements OneWireIntervalLoggable {
	public OneWireThermometer() {

	}
	
	public OneWireThermometer(double defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
	@Override
	public void updateValue() throws FileNotFoundException {
		double value = getDoubleVar("temperature");
		addHistoryPoint(value);
	}
	
	public void addHistoryPoint(double value) {
		DoubleHistoryPoint dhp = new DoubleHistoryPoint();
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
