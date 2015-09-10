package com.homeki.core.device.onewire;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;

import javax.persistence.Entity;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OneWireThermocouple extends OneWireDevice implements OneWireIntervalLoggable {
	private static final int TEMPERATURE_CHANNEL = 0;
	
	public OneWireThermocouple() {

	}
	
	public OneWireThermocouple(double defaultValue) {
		addHistoryPoint(TEMPERATURE_CHANNEL, defaultValue);
	}
	
	@Override
	public void updateValue() throws FileNotFoundException {
		double value = getDoubleVar("typeK/temperature"); // Path to file with temperature
		addHistoryPoint(TEMPERATURE_CHANNEL, value);
	}
	
	@Override
	public String getType() {
		return "thermocouple";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(TEMPERATURE_CHANNEL, "Temperature", DataType.DOUBLE, "°C", 1.0));
		return list;
	}
}
