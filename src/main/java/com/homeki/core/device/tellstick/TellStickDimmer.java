package com.homeki.core.device.tellstick;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;
import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.Settable;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TellStickDimmer extends TellStickDevice implements Settable, TellStickLearnable {
	public static final int ONOFF_CHANNEL = 0;
	public static final int LEVEL_CHANNEL = 1;
	
	public TellStickDimmer() {
		
	}
	
	public TellStickDimmer(int defaultLevel) {
		addHistoryPoint(ONOFF_CHANNEL, 0);
		addHistoryPoint(LEVEL_CHANNEL, defaultLevel);
	}
	
	public TellStickDimmer(int defaultLevel, String protocol, String model, String house, String unit) {
		this(defaultLevel);
		
		int result = TellStickApi.INSTANCE.addDevice(protocol, model, house, unit);
		
		this.internalId = String.valueOf(result);
	}

	@Override
	public void set(int channel, int value) {
		getChannel(channel);
		
		int internalId = Integer.parseInt(getInternalId());
		IntegerHistoryPoint level = (IntegerHistoryPoint)getLatestHistoryPoint(LEVEL_CHANNEL);
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)getLatestHistoryPoint(ONOFF_CHANNEL);
		
		if (channel == ONOFF_CHANNEL) {
			boolean on = value > 0;
			if (on) {
				TellStickApi.INSTANCE.dim(internalId, level.getValue());
				addHistoryPoint(ONOFF_CHANNEL, 1);
			} else {
				TellStickApi.INSTANCE.turnOff(internalId);
			}
		} else if (channel == LEVEL_CHANNEL) {
			if (onoff.getValue() > 0)
				TellStickApi.INSTANCE.dim(internalId, value);
			else
				addHistoryPoint(LEVEL_CHANNEL, value);
		}
	}

	@Override
	public String getType() {
		return "dimmer";
	}

	@Override
	public void learn() {
		TellStickApi.INSTANCE.learn(Integer.valueOf(internalId));
	}
	
	@Override
	public void preDelete() {
		TellStickApi.INSTANCE.removeDevice(Integer.valueOf(internalId));
	}

	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<>();
		list.add(new Channel(ONOFF_CHANNEL, "Switch", DataType.INT));
		list.add(new Channel(LEVEL_CHANNEL, "Level", DataType.BYTE));
		return list;
	}
}
