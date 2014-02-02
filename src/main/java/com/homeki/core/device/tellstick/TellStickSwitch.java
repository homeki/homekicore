package com.homeki.core.device.tellstick;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Settable;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TellStickSwitch extends TellStickDevice implements Settable, TellStickLearnable {
	public static final int ONOFF_CHANNEL = 0;
	
	public TellStickSwitch() {
		
	}
	
	public TellStickSwitch(boolean defaultValue) {
		addHistoryPoint(ONOFF_CHANNEL, defaultValue ? 1 : 0);
	}
	
	public TellStickSwitch(boolean defaultValue, int house, int unit) {
		this(defaultValue);
		
		int result = TellStickApi.INSTANCE.addSwitch(house, unit);
		
		this.internalId = String.valueOf(result);
	}
	
	@Override
	public void set(int channel, int value) {
		validateChannel(channel);
		
		boolean on = value > 0;
		int internalId = Integer.parseInt(getInternalId());
		
		if (on)
			TellStickApi.INSTANCE.turnOn(internalId);
		else
			TellStickApi.INSTANCE.turnOff(internalId);
	}

	@Override
	public String getType() {
		return "switch";
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
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(ONOFF_CHANNEL, "onoff", Channel.INT));
		return list;
	}
}
