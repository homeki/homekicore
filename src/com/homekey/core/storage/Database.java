package com.homekey.core.storage;

public abstract class Database {
	public final String DEVICE_TABLE_NAME_PREFIX = "d_";
	
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
	public abstract void updateRow(String table, String[] updateColumns, Object[] updateValues, String whereColumn, Object whereValue);
	public abstract Object[] getRow(String table, String[] selectColumns, String whereColumn, Object whereValue) ;
	public abstract Object getField(String table, String selectColumn, String whereColumn, Object whereValue);
	public abstract Object getField(String table, String column, String orderByColumn);
	
	public abstract void createTable(String name, DatabaseTable table);
	public abstract boolean tableExists(String name);
	
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
