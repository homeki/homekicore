package com.homekey.core.storage;

public class DatabaseTable {
	private String[] names;
	private ColumnType[] types;
	
	public DatabaseTable(int columnCount) {
		names = new String[columnCount];
		types = new ColumnType[columnCount];
	}
	
	public void setColumn(int i, String name, ColumnType type) {
		names[i] = name;
		types[i] = type;
	}
	
	public String getName(int i) {
		return names[i];
	}
	
	public ColumnType getType(int i) {
		return types[i];
	}
	
	public int getColumnCount() {
		return names.length;
	}
}
