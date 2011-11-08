package com.homeki.core.device.mock;

import java.util.Date;

import com.homeki.core.device.Device;
import com.homeki.core.storage.ITableFactory;

public abstract class MockDevice extends Device {
	private Date fakeAdded;

	public MockDevice(String internalId, ITableFactory factory) {
		super(internalId, factory);
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
