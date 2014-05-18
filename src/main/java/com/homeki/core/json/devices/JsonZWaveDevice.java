package com.homeki.core.json.devices;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.device.Device;

@JsonTypeName("zwave")
public class JsonZWaveDevice extends JsonDevice {
	public JsonZWaveDevice() {

	}

	public JsonZWaveDevice(Device d) {
		super(d);
	}
}
