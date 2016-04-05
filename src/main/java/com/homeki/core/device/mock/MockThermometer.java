package com.homeki.core.device.mock;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;
import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.device.onewire.OneWireIntervalLoggable;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonMockDevice;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

@Entity
public class MockThermometer extends Device implements OneWireIntervalLoggable {
	private static final int TEMPERATURE_CHANNEL = 0;
	
	@Transient
	private Random rnd;
	
	public MockThermometer() {
		rnd = new Random();
	}
	
	public MockThermometer(double defaultValue) {
		this();
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
			double value = getRandomThermometerValue();
			addHistoryPoint(TEMPERATURE_CHANNEL, value);
		}
	}
	
	private Double getRandomThermometerValue() {
		long sleepTime = 1000 + (rnd.nextInt(500) - 250);
		
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { }
		
		return (rnd.nextDouble() * 2 - 1) * 40;
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

	@Override
	public JsonDevice toJson() {
		return new JsonMockDevice(this);
	}
}
