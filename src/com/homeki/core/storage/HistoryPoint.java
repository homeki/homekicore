package com.homeki.core.storage;

import java.util.Date;

import com.homeki.core.device.Device;

public abstract class HistoryPoint {
	private int id;
	private Device device;
	private Date registered;
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	public int getId() {
		return this.id;
	}
	
	public abstract Object getValue();
}
