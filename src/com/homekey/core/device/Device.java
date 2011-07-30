package com.homekey.core.device;

import java.util.Date;

import com.homekey.core.log.L;
import com.homekey.core.storage.Database;

public abstract class Device {
	protected Database db;
	protected int id;
	protected String databaseTableName;
	
	public Device(String internalId, Database db) {
		this.db = db;
		
		Object field = db.getField("devices", "id", "internalid", internalId);
		
		if (field == null) {
			db.addRow("devices", new String[] { "internalid", "type", "name", "added", "active" }, new Object[] { internalId, this.getClass().getSimpleName(), "", new Date(), true });
			field = db.getField("devices", "id", "internalid", internalId);
		}
		
		id = (Integer) field;
		databaseTableName = (db.DEVICE_TABLE_NAME_PREFIX + this.getClass().getSimpleName() + "_" + id).toLowerCase();
		
		if (!db.tableExists(databaseTableName)) {
			createDatabaseTable();
		}
	}
	
	public void setName(String name) {
		setVar("name", name);
	}
	
	private void setVar(String column, Object val) {
		db.updateRow("devices", new String[] { column }, new Object[] { val }, "id", id);
	}
	
	public void setActive(boolean active) {
		setVar("active", active);
	}
	
	public String getName() {
		return (String) getVar("name");
	}
	
	public Object getVar(String column) {
		return db.getField("devices", column, "id", id);
	}
	
	public int getId() {
		return id;
	}
	
	public Date getAdded() {
		Long time = (Long) getVar("added");
		return new Date( time);
	}
	
	public boolean isActive() {
		L.setStandard("asdf");
		L.d("getvar=" + getVar("active"));
		return  getVar("active").equals("true");
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Device) obj).id;
	}
	
	public String getInternalId() {
		return (String) getVar("internalid");
	}
	
	protected abstract void createDatabaseTable();
}
