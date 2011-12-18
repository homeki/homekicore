package com.homeki.core.storage.entities;

import java.util.Date;

public class HDimmerHistory {
	private Integer id;
	private HDevice device;
	private Date registered;
	private Integer value;
	
	public Integer getId() {
		return id;
	}
	
	public HDevice getDevice() {
		return device;
	}

	public void setDevice(HDevice device) {
		this.device = device;
	}
	
	public Date getRegistered() {
		return registered;
	}
	
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
}
