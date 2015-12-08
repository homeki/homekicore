package com.homeki.core.device.onewire;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;

import javax.persistence.Entity;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OneWireCounter extends OneWireDevice implements OneWireIntervalLoggable {
	private static final int COUNTER_CHANNEL_A = 0;
	private static final int COUNTER_CHANNEL_B = 1;
	
	public OneWireCounter() {

	}
	
	public OneWireCounter(double defaultValue) {
		addHistoryPoint(COUNTER_CHANNEL_A, defaultValue);
		addHistoryPoint(COUNTER_CHANNEL_B, defaultValue);		
	}
	
	@Override
	public void updateValue() throws FileNotFoundException {
		double value = getIntegerVar("counters.A");
		addHistoryPoint(COUNTER_CHANNEL_A, value);  		
		value = getIntegerVar("counters.B");
		addHistoryPoint(COUNTER_CHANNEL_B, value);
	}
	
	@Override
	public String getType() {
		return "counter";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(COUNTER_CHANNEL_A, "Counter", DataType.INT, "", 1.0));
		list.add(new Channel(COUNTER_CHANNEL_B, "Counter", DataType.INT, "", 1.0));
		return list;
	}
}
