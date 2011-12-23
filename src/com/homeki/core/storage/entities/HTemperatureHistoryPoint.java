package com.homeki.core.storage.entities;

import java.util.Date;

import com.homeki.core.storage.HistoryPoint;

public class HTemperatureHistoryPoint implements HistoryPoint {
	private Integer id;
	private HDevice device;
	private Date registered;
	private Double value;
	
	public HTemperatureHistoryPoint(Double value) {
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
	
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
}
