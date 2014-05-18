package com.homeki.core.device.mock;

import com.homeki.core.device.Channel;
import com.homeki.core.device.DataType;
import com.homeki.core.device.Device;
import com.homeki.core.device.Settable;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonMockDevice;
import com.homeki.core.logging.L;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MockDimmer extends Device implements Settable {
	public static final int ONOFF_CHANNEL = 0;
	public static final int LEVEL_CHANNEL = 1;
	
	public MockDimmer() {
		
	}
	
	public MockDimmer(int defaultLevel) {
		addHistoryPoint(ONOFF_CHANNEL, 0);
		addHistoryPoint(LEVEL_CHANNEL, defaultLevel);
	}

	@Override
	public void set(int channel, int value) {
		getChannel(channel);
		
		if (channel == ONOFF_CHANNEL)
			addHistoryPoint(ONOFF_CHANNEL, value > 0 ? 1 : 0);
		else if (channel == LEVEL_CHANNEL)
			addHistoryPoint(LEVEL_CHANNEL, value);
		
		L.i("MockDimmer with internal ID '" + getInternalId() + "' changed channel " + channel + " to " + value + ".");
	}

	@Override
	public String getType() {
		return "dimmer";
	}
	
	@Override
	public List<Channel> getChannels() {
		List<Channel> list = new ArrayList<Channel>();
		list.add(new Channel(ONOFF_CHANNEL, "onoff", DataType.INT));
		list.add(new Channel(LEVEL_CHANNEL, "level", DataType.BYTE));
		return list;
	}

	@Override
	public JsonDevice toJson() {
		return new JsonMockDevice(this);
	}
}
