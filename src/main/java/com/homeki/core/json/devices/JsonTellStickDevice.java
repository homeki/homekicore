package com.homeki.core.json.devices;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.device.Device;

@JsonTypeName("tellstick")
public class JsonTellStickDevice extends JsonDevice {
	public Integer house;
	public Integer unit;

	public JsonTellStickDevice() {

	}
	
	public JsonTellStickDevice(Device d) {
		super(d);
	}
}
