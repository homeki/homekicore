package com.homekey.core.storage.impl;

import com.homekey.core.device.Device;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class MockDatabase extends Database {
	@Override
	public void close() {
		
	}

	@Override
	protected void open() {

	}

	@Override
	protected void addDevice(Device device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadDevice(Device device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean deviceExists(Device device) {
		// TODO Auto-generated method stub
		return false;
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
	
}
