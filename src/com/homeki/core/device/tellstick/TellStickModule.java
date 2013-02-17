package com.homeki.core.device.tellstick;

import java.util.List;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.logging.L;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Module;
import com.homeki.core.storage.Hibernate;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	
	public TellStickModule() {

	}
	
	@Override
	public void construct() {
		Session session = Hibernate.openSession();
		try {
			@SuppressWarnings("unchecked")
			List<TellStickDevice> devices = session.createCriteria(TellStickDevice.class).list();
			
			for (TellStickDevice d : devices)
				d.setActive(false);
			
			TellStickApi.INSTANCE.open();
			int[] nativeIds = TellStickApi.INSTANCE.getDeviceIds();
			
			syncWithDb(session, nativeIds, devices);
		} finally {
			Hibernate.closeSession(session);
		}
		
		listenerThread = new TellStickListenerThread();
		listenerThread.start();
	}

	@Override
	public void destruct() {
		listenerThread.shutdown();
		TellStickApi.INSTANCE.close();
	}
	
	private void syncWithDb(Session session, int[] nativeIds, List<TellStickDevice> devices) {
		if (nativeIds == null)
			return;
	
		for (int id : nativeIds) {
			String sid = String.valueOf(id);
			String type = TellStickApi.INSTANCE.getDeviceType(id);
			
			Device dev = existsInList(devices, sid);
			
			if (dev == null) {
				if (type.equals("switch")) {
					dev = new TellStickSwitch(false);
				} else if (type.equals("dimmer")) {
					dev = new TellStickDimmer(255);
				} else {
					L.e("Found no corresponding device for TellStick device with internal ID '" + sid + "' and type '" + type + "'.");
					continue;
				}
				dev.setInternalId(sid);
				session.save(dev);
			}
			
			dev.setActive(true);
		}
	}
	
	private TellStickDevice existsInList(List<TellStickDevice> devices, String internalId) {
		for (TellStickDevice d : devices) {
			if (d.getInternalId().equals(internalId))
				return d;
		}
		return null;
	}
}
