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
		double value = getDoubleVar("typeK/temperature"); // Sökväg till fil med temperatur
		addHistoryPoint(TEMPERATURE_CHANNEL, value * scale);
	}
	
	@Override
	public String getType() {
		return "thermocouple";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(TEMPERATURE_CHANNEL, "Temperature", DataType.DOUBLE));
		return list;
	}
}
