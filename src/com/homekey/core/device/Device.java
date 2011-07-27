package com.homekey.core.device;

import java.util.Calendar;
import java.util.Date;

import com.homekey.core.storage.DatabaseTable;

public abstract class Device {
	protected int id;
	protected String name;
	protected String internalId;
	protected Date added;
	protected boolean active;
	
	public Device(String internalId, String name) {
		this.name = name;
		this.internalId = internalId;
		this.active = true;
		this.added = Calendar.getInstance().getTime();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getInternalId() {
		return internalId;
	}
	
	public Date getAdded() {
		return added;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	public abstract DatabaseTable getTableDesign();
}
