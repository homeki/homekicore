package com.homekey.core.device;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DeviceInformation {
	private String internalId;
	private Type type;
	private Map<String, String> additionalData;
	
	public DeviceInformation(String internalId, Type type) {
		this.internalId = internalId;
		this.type = type;
		this.additionalData = new HashMap<String, String>();
	}
	
	public String getInternalId() {
		return internalId;
	}
	
	public Type getType() {
		return type;
	}
	
	public void addAdditionalData(String key, String value) {
		additionalData.put(key, value);
	}
	
	public String getAdditionalData(String key) {
		return additionalData.get(key);
	}
}
