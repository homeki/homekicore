package com.homekey.core.device;

import java.util.Date;

import com.homekey.core.storage.DatabaseTable;

public abstract class Device {
	protected int id;
	
	public Device(String internalId) {
		//TODO: look in db
	}
	
	public void setName(String name) {
		//TODO: look in db

	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setActive(boolean active) {
	}
	
	public String getName() {
		return null;
	}
	
	public int getId() {
		return id;
	}
	
	public String getInternalId() {
		return null;
	}
	
	public Date getAdded() {
		return null;
	}
	
	public boolean isActive() {
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Device)obj).id;
	}
	
	public abstract DatabaseTable getTableDesign();
	
	public abstract Object[] getDataRow();
}
 