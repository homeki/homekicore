package com.homekey.core.storage;

public abstract class Database {
	protected final String SENSOR_TABLE_PREFIX = "D_";
	protected static final String DEFAULT_DATABASE_NAME = "homekey.db";
	
	protected String databaseName;
	
	protected Database(String databaseName) {
		this.databaseName = databaseName;
		open();
		ensureSystemTables();
	}
	
	protected Database() {
		this.databaseName = DEFAULT_DATABASE_NAME;
		open();
		ensureSystemTables();
	}
	
	public abstract void close();

	protected abstract void open();

	public abstract void addRow(String table, String[] columns, Object[] values);
	public abstract void updateRow(String table, String[] columns, Object[] values);
	public abstract Object[] getFields(String table, String[] columns, Object value);
	public abstract <T> T getField(String table, String[] columns, Object value);
	
	protected abstract void createTable(String name, DatabaseTable table);
	protected abstract boolean tableExists(String name);
	
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
}
