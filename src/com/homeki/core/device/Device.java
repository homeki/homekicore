package com.homeki.core.device;

import java.util.Date;

import com.homeki.core.log.L;
import com.homeki.core.storage.IDeviceTable;
import com.homeki.core.storage.IHistoryTable;
import com.homeki.core.storage.ITableFactory;

public abstract class Device {
	protected final int id;
	
	private IDeviceTable deviceTable;
	protected IHistoryTable historyTable;
	
	public Device(String internalId, ITableFactory factory) {
		deviceTable = factory.getDeviceTable();
		
		// if a row doesn't exist for the device, create one. 
		// else, just load the id for the row
		if (!deviceTable.rowExists(internalId)) {
			id = deviceTable.createRow(internalId, this.getClass().getSimpleName());
		}
		else {
			id = deviceTable.getId(internalId);
		}
		
		ensureHistoryTable(factory, ("D_" + this.getClass().getSimpleName() + "_" + id).toLowerCase());
	}
	
	public void setName(String name) {
		deviceTable.setName(id, name);
	}
	
	public void setActive(boolean active) {
		deviceTable.setActive(id, active);
	}
	
	public String getName() {
		return deviceTable.getName(id);
	}
	
	public int getId() {
		return id;
	}
	
	public Date getAdded() {
		return deviceTable.getAdded(id);
	}
	
	public boolean isActive() {
		return deviceTable.isActive(id);
	}
	
	public String getInternalId() {
		return deviceTable.getInternalId(id);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Device) obj).id;
	}
	
	protected abstract void ensureHistoryTable(ITableFactory factory, String tableName);
}
