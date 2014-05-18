package com.homeki.core.device.zwave;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonZWaveDevice;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ZWaveDevice extends Device {
	@Override
	public String getType() {
		Set<String> switchNames = new HashSet<String>() {{
			add("Switch");
		}};
		Set<String> dimmerNames = new HashSet<String>() {{
			add("Switch");
			add("Level");
		}};
		Set<String> switchMeterNames = new HashSet<String>() {{
			add("Switch");
			add("Power");
			add("Voltage");
			add("Current");
		}};
		Set<String> dimmerMeterNames = new HashSet<String>() {{
			add("Switch");
			add("Level");
			add("Power");
			add("Voltage");
			add("Current");
		}};

		if (channelsContains(dimmerMeterNames)) return "dimmermeter";
		if (channelsContains(switchMeterNames)) return "switchmeter";
		if (channelsContains(dimmerNames)) return "dimmer";
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
}
