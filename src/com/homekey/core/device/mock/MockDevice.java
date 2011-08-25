package com.homekey.core.device.mock;

import java.util.Date;

import com.homekey.core.device.Device;
import com.homekey.core.storage.ITableFactory;

public abstract class MockDevice extends Device {
	private Date fakeAdded;

	public MockDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}

	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		// no history for a simple mockdevice
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
