package com.homeki.core.device.zwave;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.Settable;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonZWaveDevice;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ZWaveDevice extends Device implements Settable {
	@Override
	public String getType() {
		Set<String> switchNames = new HashSet<String>() {{
			add("Switch");
		}};
		Set<String> switchMeterNames = new HashSet<String>() {{
			add("Switch");
			add("Power");
			add("Voltage");
			add("Current");
		}};

		if (channelsContains(switchMeterNames)) return "switchmeter";
		if (channelsContains(switchNames)) return "switch";
		return "unknown";
	}

	@Override
	public List<Channel> getChannels() {
		return channels;
	}

	@Override
	public JsonDevice toJson() {
		return new JsonZWaveDevice(this);
	}

	private boolean channelsContains(Set<String> names) {
		for (Channel c : channels) {
			names.remove(c.getName());
		}

		return names.size() == 0;
	}

	@Override
	public void set(int channel, int value) {
		ZWaveChannel c = (ZWaveChannel)getChannel(channel);
		short nodeId = Short.parseShort(internalId.replace("zw-", ""));
		ZWaveApi.INSTANCE.setValue(c.getCommandClassId(), nodeId, c.getGenre(), c.getIndex(), c.getInstance(), c.getValueType(), value);
	}
}
