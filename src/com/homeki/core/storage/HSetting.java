package com.homeki.core.storage;

public class HSetting {
	private Integer id;
	private String key;
	private String value;
	
	public Integer getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
