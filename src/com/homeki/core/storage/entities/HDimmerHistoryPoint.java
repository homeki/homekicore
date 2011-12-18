package com.homeki.core.storage.entities;

import java.util.Date;

import com.homeki.core.storage.HistoryPoint;

public class HDimmerHistoryPoint implements HistoryPoint {
	private Integer id;
	private HDevice device;
	private Date registered;
	private Integer value;
	
	public HDimmerHistoryPoint(Integer value) {
		this.registered = new Date();
		this.value = value;
	}
	
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
	
	public Integer getValue() {
		return value;
	}
}
