package com.homeki.core.device.mock;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.Settable;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonMockDevice;
import com.homeki.core.logging.L;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MockSwitch extends Device implements Settable {
	private static final int ONOFF_CHANNEL = 0;
	
	public MockSwitch() {
		
	}
	
	public MockSwitch(int id) {
		this.id = id;
	}
	
	public MockSwitch(boolean defaultValue) {
		addHistoryPoint(ONOFF_CHANNEL, defaultValue ? 1 : 0);
	}

	@Override
	public void set(int channel, int value) {
		validateChannel(channel);
		addHistoryPoint(ONOFF_CHANNEL, value > 0 ? 1 : 0);
		L.i("MockSwitch with internal ID '" + getInternalId() + "' changed channel " + channel + " to " + value + ".");
	}
	
	@Override
	public String getType() {
		return "switch";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(ONOFF_CHANNEL, "onoff", Channel.INT));
		return list;
	}

	@Override
	public JsonDevice toJson() {
		return new JsonMockDevice(this);
	}
}
