package com.homeki.core.device.onewire;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;
import com.homeki.core.device.HistoryPoint;

import javax.persistence.Entity;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Entity
public class OneWireThermometer extends OneWireDevice implements OneWireIntervalLoggable {
	private static final int TEMPERATURE_CHANNEL = 0;
	
	public OneWireThermometer() {

	}
	
	public OneWireThermometer(double defaultValue) {
		addHistoryPoint(TEMPERATURE_CHANNEL, defaultValue);
	}
	
	@Override
	public void updateValue() throws FileNotFoundException {
		boolean skipLogging = false;
		HistoryPoint hp = this.getLatestHistoryPoint(TEMPERATURE_CHANNEL);
		if (hp != null) {
			Date dateNow = new Date();
			long diff = dateNow.getTime() - hp.getRegistered().getTime();
			if(diff < this.getLoggingInterval()) {
				skipLogging = true;
			}
		}
		if (!skipLogging) {
			String temperaturePath = getStringVar("type").equals("DS2760") ? "typeK/temperature" : "temperature";
			double value = getDoubleVar(temperaturePath);
			addHistoryPoint(TEMPERATURE_CHANNEL, value);
		}
	}
	
	@Override
	public String getType() {
		return "thermometer";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(TEMPERATURE_CHANNEL, "Temperature", DataType.DOUBLE));
		return list;
	}
}
