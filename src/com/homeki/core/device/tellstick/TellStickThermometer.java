package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.DoubleHistoryPoint;

@Entity
public class TellStickThermometer extends Device {
	public TellStickThermometer() {
		
	}
	
	public TellStickThermometer(double defaultValue) {
		addHistoryPoint(defaultValue);
	}
	
	@Override
	public String getType() {
		return "thermometer";
	}
	
	public void addHistoryPoint(double value) {
		DoubleHistoryPoint thp = new DoubleHistoryPoint();
		thp.setDevice(this);
		thp.setRegistered(new Date());
		thp.setValue(value);
		historyPoints.add(thp);
	}
}
