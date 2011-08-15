package com.homekey.core.device.mock;

import java.util.Date;

import com.homekey.core.device.Device;
import com.homekey.core.storage.Database;

public abstract class MockDevice extends Device {
	private Date fakeAdded;

	public MockDevice(String internalId, Database db) {
		super(internalId, db);
	}

	@Override
	protected void createDatabaseTable() {
		// no need for a table when we won't have any specific data for the device
	}
	
	public void setPretendAddedDate(Date date){
		this.fakeAdded = date;
	}
	
	public Date getAdded() {
		if(fakeAdded != null)
			return fakeAdded;
		return super.getAdded();
	}
}
