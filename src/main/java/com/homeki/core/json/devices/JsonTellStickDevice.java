package com.homeki.core.json.devices;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.device.Device;

@JsonTypeName("tellstick")
public class JsonTellStickDevice extends JsonDevice {
	public String protocol;
	public String model;
	public String house;
	public String unit;

	public JsonTellStickDevice() {

	}
	
	public JsonTellStickDevice(Device d) {
		super(d);
	}
}
