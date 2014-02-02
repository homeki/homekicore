package com.homeki.core.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.json.devices.JsonTellStickDevice;

import javax.persistence.Entity;

@Entity
public abstract class TellStickDevice extends Device {
	@Override
	public JsonDevice toJson() {
		return new JsonTellStickDevice(this);
	}
}
