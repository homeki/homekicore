package com.homeki.core.http.json;

import com.homeki.core.device.Device;

public class JsonTellStickDevice extends JsonDevice {
	public Integer house;
	public Integer unit;

	public JsonTellStickDevice() {

	}
	
	public JsonTellStickDevice(Device d) {
		super(d);
	}
}
