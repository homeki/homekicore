package com.homeki.core.json.devices;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.device.Device;

@JsonTypeName("mock")
public class JsonMockDevice extends JsonDevice {
	public JsonMockDevice() {

	}

	public JsonMockDevice(Device d) {
		super(d);
	}
}
