package com.homeki.core.device;

import java.util.HashMap;
import java.util.Map;

public class DeviceInformation {
	public enum DeviceType {
		MockSwitch, 
		MockDimmer,
		MockThermometer,
		TellStickSwitch,
		TellStickDimmer,
		OneWireThermometer,
		Camera
	}
	
	private String internalId;
	private DeviceType type;
	private Map<String, String> additionalData;
	
	public DeviceInformation(String internalId, DeviceType type) {
		this.internalId = internalId;
		this.type = type;
		this.additionalData = new HashMap<String, String>();
	}
	
	public String getInternalId() {
		return internalId;
	}
	
	public DeviceType getType() {
		return type;
	}
	
	public void addAdditionalData(String key, String value) {
		additionalData.put(key, value);
	}
	
	public String getAdditionalData(String key) {
		return additionalData.get(key);
	}
}
