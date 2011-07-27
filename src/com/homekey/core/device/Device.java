package com.homekey.core.device;

import com.homekey.core.storage.DatabaseTable;

public abstract class Device {
	protected int id;
	protected String name;
	protected String internalId;

	public Device(int id,String internalId, String name) {
		this.id = id;
		this.name = name;
		this.internalId = internalId;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public String getInternalId(){
		return internalId;
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
