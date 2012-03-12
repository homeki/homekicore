package com.homeki.core.http.json;

import org.hibernate.Session;

import com.homeki.core.device.Device;

public class JsonTellStickDevice extends JsonDevice {
	public Integer house;
	public Integer unit;
	
	public JsonTellStickDevice(Device d, Session session) {
		super(session, d);
	}
}
