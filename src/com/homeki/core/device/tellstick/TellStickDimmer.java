package com.homeki.core.device.tellstick;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import com.homeki.core.device.Channel;
import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.Settable;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class TellStickDimmer extends TellStickDevice implements Settable, TellStickLearnable {
	public static final int ONOFF_CHANNEL = 0;
	public static final int LEVEL_CHANNEL = 1;
	
	public TellStickDimmer() {
		
	}
	
	public TellStickDimmer(int defaultLevel) {
		addOnOffHistoryPoint(false);
		addLevelHistoryPoint(defaultLevel);
	}
	
	public TellStickDimmer(int defaultLevel, int house, int unit) {
		this(defaultLevel);
		
		int result = TellStickNative.addDimmer(house, unit);
		
		this.internalId = String.valueOf(result);
	}

	@Override
	public void set(int channel, int value) {
		int internalId = Integer.parseInt(getInternalId());
		IntegerHistoryPoint level = (IntegerHistoryPoint)getLatestHistoryPoint(LEVEL_CHANNEL);
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)getLatestHistoryPoint(ONOFF_CHANNEL);
		
		if (channel == ONOFF_CHANNEL) {
			boolean on = value > 0;
			if (on) {
				TellStickNative.dim(internalId, level.getValue());
				addOnOffHistoryPoint(true);
			} else {
				TellStickNative.turnOff(internalId);
			}
		} else if (channel == LEVEL_CHANNEL) {
			if (onoff.getValue() > 0)
				TellStickNative.dim(internalId, value);
			else
				addLevelHistoryPoint(value);
		} else {
			throw new RuntimeException("Tried to set invalid channel " + channel + " on TellStickDimmer '" + getInternalId() + "'.");
		}
	}

	@Override
	public String getType() {
		return "dimmer";
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
	public void learn() {
		TellStickNative.learn(Integer.valueOf(internalId));
	}
	
	@Override
	public void preDelete() {
		TellStickNative.removeDevice(Integer.valueOf(internalId));
	}
	
	@Override
	public String[] getAbilities() {
		return new String[] { "tellstick" };
	}

	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(ONOFF_CHANNEL, "onoff", Channel.INT));
		list.add(new Channel(LEVEL_CHANNEL, "level", Channel.BYTE));
		return list;
	}
}
