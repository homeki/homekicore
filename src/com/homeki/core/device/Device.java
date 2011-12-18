package com.homeki.core.device;

import java.lang.reflect.Type;
import java.util.Date;

import org.hibernate.Session;

import com.homeki.core.storage.HDevice;
import com.homeki.core.storage.Hibernate;

public abstract class Device {
	protected final int id;
	
	public Device(String internalId) {
		Session session = Hibernate.openSession();
		Object deviceId = session.createQuery("from HDevice as dev where dev.internalId = ?").setString(0, internalId).uniqueResult();
		
		// if a row doesn't exist for the device, create one. 
		// else, just load the id for the row
		if (deviceId == null) {
			HDevice dev = new HDevice();
			dev.setInternalId(internalId);
			id = (Integer)session.save(dev);
		}
		else {
			id = (Integer)deviceId;
		}
		
		Hibernate.commitSession(session);
	}
	
	public void setName(String name) {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		dev.setName(name);
		Hibernate.commitSession(session);
	}
	
	public String getName() {
		return "";
	}
	
	public int getId() {
		return id;
	}
	
	public Date getAdded() {
		return null;
	}
	
	public String getInternalId() {
		return "";
	}
	
	@Override
	public int hashCode() {
		return (int)(id % Integer.MAX_VALUE);
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Device) obj).id;
	}
	
	public abstract String getType();
	
	protected abstract Type getTableValueType();
}
