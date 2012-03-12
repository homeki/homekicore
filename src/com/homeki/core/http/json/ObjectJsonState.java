package com.homeki.core.http.json;

import com.homeki.core.device.Device;

public class ObjectJsonState extends JsonState {
	public Object value;
	
	public ObjectJsonState(Device d) {
		value = d.getLatestHistoryPoint(0).getValue();
	}
}
