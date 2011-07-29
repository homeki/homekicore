package com.homekey.core.storage.mock;

import java.util.Date;

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
	public void updateRow(String table, String[] updateColumns, Object[] updateValues, String whereColumn, Object whereValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getRow(String table, String[] selectColumns, String whereColumn, Object whereValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getField(String table, String selectColumn, String whereColumn, Object whereValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getField(String table, String column, String orderByColumn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createTable(String name, DatabaseTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean tableExists(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
