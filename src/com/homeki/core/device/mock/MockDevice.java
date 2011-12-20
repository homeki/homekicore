package com.homeki.core.device.mock;

import java.util.Date;

import com.homeki.core.device.Device;

public abstract class MockDevice extends Device {
	private Date fakeAdded;

	public MockDevice(String internalId) {
		super(internalId);
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
