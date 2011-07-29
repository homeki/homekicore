package com.homekey.core.storage.impl;

import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class MockDatabase extends Database {
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRow(String table, String[] columns, Object[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRow(String table, String[] columns, Object[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getFields(String table, String[] columns, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createTable(String name, DatabaseTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean tableExists(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T getField(String table, String[] columns, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
