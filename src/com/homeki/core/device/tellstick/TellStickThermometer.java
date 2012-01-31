package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.TemperatureHistoryPoint;

@Entity
public class TellStickThermometer extends Device {
	@Override
	public String getType() {
		return "thermometer";
	}
	
	public void addHistoryValue(double value) {
		TemperatureHistoryPoint thp = new TemperatureHistoryPoint();
		thp.setDevice(this);
		thp.setRegistered(new Date());
		thp.setValue(value);
		historyPoints.add(thp);
	}
}
