package com.homeki.core.device;

import java.lang.reflect.Type;
import java.util.Date;

import org.hibernate.Session;

import com.homeki.core.storage.HDevice;
import com.homeki.core.storage.Hibernate;

public abstract class Device {
	protected final Long id;
	
	public Device(String internalId) {
		Session session = Hibernate.openSession();
		Object deviceId = session.createQuery("from HDevice where HDevice.internalid = ?").setEntity(0, internalId).uniqueResult();
		
		// if a row doesn't exist for the device, create one. 
		// else, just load the id for the row
		if (deviceId == null) {
			HDevice dev = new HDevice();
			dev.setInternalId(internalId);
			session.save(dev);
			id = dev.getId();
		}
		else {
			id = (Long)deviceId;
		}
		
		Hibernate.commitSession(session);
	}
	
	public void setName(String name) {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		dev.setName(name);
		Hibernate.commitSession(session);
	}
	
	public void setActive(boolean active) {
		deviceTable.setActive(id, active);
	}
	
	public String getName() {
		return deviceTable.getName(id);
	}
	
	public int getId() {
		return id;
	}
	
	public Date getAdded() {
		return deviceTable.getAdded(id);
	}
	
	public boolean isActive() {
		return deviceTable.isActive(id);
	}
	
	public String getInternalId() {
		return deviceTable.getInternalId(id);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return id == ((Device) obj).id;
	}
	
	public abstract String getType();
	
	protected abstract Type getTableValueType();
}
