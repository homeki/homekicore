package com.homekey.core.storage;

import com.homekey.core.device.Device;

public abstract class Database {
	protected final String SENSOR_TABLE_PREFIX = "D_";
	protected final String DEFAULT_DATABASE_NAME = "homekey.db";
	
	public Database() {
		open();
		ensureSystemTables();
	}
	
	public void ensureDevice(Device device) {
		if (deviceExists(device)) {
			loadDevice(device);
		}
		else {
			addDevice(device);
			createTableFor(device);
		}
	}
	
	private void createTableFor(Device device) {
		String name = SENSOR_TABLE_PREFIX + device.getClass().getSimpleName() + "_" + device.getId();
		
		createTable(name, device.getTableDesign());
	}
	
	private void ensureSystemTables() {
		if (!tableExists("devices")) {
			DatabaseTable table = new DatabaseTable(5);
		
			table.setColumn(0, "internalid", ColumnType.String);
			table.setColumn(1, "type", ColumnType.String);
			table.setColumn(2, "name", ColumnType.String);
			table.setColumn(3, "added", ColumnType.DateTime);
			table.setColumn(4, "active", ColumnType.Boolean);
			
			createTable("devices", table);
		}
	}
	
	public abstract void close();

	protected abstract void open();
	protected abstract void addDevice(Device device);
	protected abstract void loadDevice(Device device);
	protected abstract boolean deviceExists(Device device);
	protected abstract void createTable(String name, DatabaseTable table);
	protected abstract boolean tableExists(String name);
}
