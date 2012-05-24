package com.homeki.core.device.tellstick;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.abilities.Settable;
import com.homeki.core.events.ChannelChangedEvent;
import com.homeki.core.events.EventQueue;

@Entity
public class TellStickDimmer extends TellStickDevice implements Settable, TellStickLearnable {
	public static final int TELLSTICKDIMMER_ONOFF_CHANNEL = 0;
	public static final int TELLSTICKDIMMER_LEVEL_CHANNEL = 1;
	
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
		IntegerHistoryPoint level = (IntegerHistoryPoint)getLatestHistoryPoint(TELLSTICKDIMMER_LEVEL_CHANNEL);
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)getLatestHistoryPoint(TELLSTICKDIMMER_ONOFF_CHANNEL);
		
		if (channel == TELLSTICKDIMMER_ONOFF_CHANNEL) {
			boolean on = value > 0;
			if (on) {
				TellStickNative.dim(internalId, level.getValue());
				addOnOffHistoryPoint(true);
			} else {
				TellStickNative.turnOff(internalId);
			}
		} else if (channel == TELLSTICKDIMMER_LEVEL_CHANNEL) {
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
		dhp.setChannel(TELLSTICKDIMMER_ONOFF_CHANNEL);
		dhp.setValue(value);
		historyPoints.add(dhp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), TELLSTICKDIMMER_ONOFF_CHANNEL, value));
	}
	
	public void addLevelHistoryPoint(int level) {
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setChannel(TELLSTICKDIMMER_LEVEL_CHANNEL);
		dhp.setValue(level);
		historyPoints.add(dhp);
		EventQueue.getInstance().add(new ChannelChangedEvent(getId(), TELLSTICKDIMMER_LEVEL_CHANNEL, level));
	}

	@Override
	public void learn() {
		TellStickNative.learn(Integer.valueOf(internalId));
	}
	
	@Override
	public void preDelete() {
		TellStickNative.removeDevice(Integer.valueOf(internalId));
	}
}
