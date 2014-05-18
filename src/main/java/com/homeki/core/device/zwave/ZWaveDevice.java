package com.homeki.core.device.zwave;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ZWaveDevice extends Device {
	@Override
	public String getType() {
		return "notdecidedyet";
	}

	@Override
	public List<Channel> getChannels() {
		return new ArrayList<>();
	}
}
