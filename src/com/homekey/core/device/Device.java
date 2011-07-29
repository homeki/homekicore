package com.homekey.core.device;

import java.util.Date;

import com.homekey.core.storage.Database;
import com.homekey.core.storage.impl.SqliteDatabase;

public abstract class Device {
	protected static Database db = SqliteDatabase.getInstance();
	
	protected int id;
	protected String databaseTableName;
	
	public Device(String internalId) {
		Object[] fields = db.getFields("devices", new String[] { "id", "internalid" }, internalId);
		
		if (fields == null) {
			db.addRow("devices", new String[] { "internalid", "type", "name", "added", "active" }, new Object[] { internalId, this.getClass().getSimpleName(), "", new Date(), true });
			fields = db.getFields("devices", new String[] { "id", "internalid" }, internalId);
		}
		
		id = (Integer)fields[0];
		databaseTableName = db.DEVICE_TABLE_NAME_PREFIX + this.getClass().getSimpleName() + "_" + id;
		
		if (!db.tableExists(databaseTableName)) {
			createDatabaseTable();
		}
	} 
	
	public void setName(String name) {
		db.updateRow("devices", new String[] { "name", "id" }, new Object[] { name, id });
	}
	
	public void setActive(boolean active) {
		db.updateRow("devices", new String[] { "active", "id" }, new Object[] { active, id });
	}
	
	public String getName() {
		return db.getField("devices", new String[] { "name", "id" }, id);
	}
	
	public int getId() {
		return id;
	}
	
	public Date getAdded() {
		return db.getField("devices", new String[] { "added", "id" }, id);
	}
	
	public boolean isActive() {
		return db.getField("devices", new String[] { "active", "id" }, id);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Device)obj).id;
	}
	
	protected String getInternalId() {
		return db.getField("devices", new String[] { "internalid", "id" }, id);
	}
	
	protected abstract void createDatabaseTable();
}
 
