package com.homeki.core.device.mock;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.abilities.Settable;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class MockSwitch extends Device implements Settable {
	private static final int MOCKSWITCH_ONOFF_CHANNEL = 0;
	
	public MockSwitch() {
		
	}
	
	public MockSwitch(boolean defaultValue) {
		addHistoryPoint(defaultValue);
	}

	@Override
	public void set(int channel, int value) {
		if (channel != MOCKSWITCH_ONOFF_CHANNEL)
			throw new RuntimeException("Tried to set invalid channel " + channel + " on MockSwitch '" + getInternalId() + "'.");
		
		addHistoryPoint(value > 0);
	}
	
	public void addHistoryPoint(boolean on) {
		int value = on ? 1 : 0;
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		dhp.setChannel(MOCKSWITCH_ONOFF_CHANNEL);
		historyPoints.add(dhp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), MOCKSWITCH_ONOFF_CHANNEL, value));
	}
	
	@Override
	public String getType() {
		return "switch";
	}
}
