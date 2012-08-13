package com.homeki.core.device.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.Settable;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;
import com.homeki.core.logging.L;

@Entity
public class MockDimmer extends Device implements Settable {
	public static final int ONOFF_CHANNEL = 0;
	public static final int LEVEL_CHANNEL = 1;
	
	public MockDimmer() {
		
	}
	
	public MockDimmer(int defaultLevel) {
		addOnOffHistoryPoint(false);
		addLevelHistoryPoint(defaultLevel);
	}

	@Override
	public void set(int channel, int value) {
		L.i("MockDimmer with internal ID '" + getInternalId() + "' changed channel " + channel + " to " + value + ".");
		
		if (channel == ONOFF_CHANNEL)
			addOnOffHistoryPoint(value == 1);
		else if (channel == LEVEL_CHANNEL)
			addLevelHistoryPoint(value);
		else
			throw new RuntimeException("Tried to set invalid channel " + channel + " on MockDimmer with internal ID '" + getInternalId() + "'.");
	}
	
	public void addOnOffHistoryPoint(boolean on) {
		int value = on ? 1 : 0;
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setChannel(ONOFF_CHANNEL);
		dhp.setValue(value);
		historyPoints.add(dhp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), ONOFF_CHANNEL, value));
	}
	
	public void addLevelHistoryPoint(int level) {
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setChannel(LEVEL_CHANNEL);
		dhp.setValue(level);
		historyPoints.add(dhp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), LEVEL_CHANNEL, level));
	}

	@Override
	public String getType() {
		return "dimmer";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(ONOFF_CHANNEL, "onoff", Channel.BOOL));
		list.add(new Channel(LEVEL_CHANNEL, "level", Channel.INT));
		return list;
	}
}
