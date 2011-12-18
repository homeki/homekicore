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
		HDevice device = (HDevice)session.createQuery("from HDevice as dev where dev.internalId = ?").setString(0, internalId).uniqueResult();
		
		if (device == null) {
			device = new HDevice();
			device.setInternalId(internalId);
			device.setAdded(new Date());
			id = (Integer)session.save(device);
		}
		else {
			id = device.getId();
		}
		
		Hibernate.closeSession(session);
	}
	
	public void setName(String name) {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		dev.setName(name);
		Hibernate.closeSession(session);
	}
	
	public String getName() {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		String name = dev.getName();
		Hibernate.closeSession(session);
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Date getAdded() {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		Date added = dev.getAdded();
		Hibernate.closeSession(session);
		return added;
	}
	
	public String getInternalId() {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		String internalId = dev.getInternalId();
		Hibernate.closeSession(session);
		return internalId;
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
