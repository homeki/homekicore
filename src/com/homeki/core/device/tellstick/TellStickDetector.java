package com.homeki.core.device.tellstick;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class TellStickDetector extends ControlledThread {
	public TellStickDetector(int interval) {
		super(interval);
	}

	@Override
	protected void iteration() throws InterruptedException {
		int[] ids = TellStickNative.getDeviceIds();
		Session session = Hibernate.openSession();
		
		for (int i = 0; i < ids.length; i++) {
			int id = ids[i];
			String internalId = String.valueOf(id);
			Device dev = Device.getByInternalId(session, internalId);
			
			if (dev == null) {
				String type = TellStickNative.getDeviceType(id);
				
				if (type.equals("dimmer")) {
					dev = new TellStickDimmer(0);
				} else if (type.equals("switch")) {
					dev = new TellStickSwitch(false);
				} else {
					L.w("Found no corresponding device for TellStick device type " + type + ".");
					continue;
				}
				
				dev.setInternalId(internalId);
				
				session.save(dev);
			}
		}
		
		/*for (int id : allowedSensorIds) {
			String internalId = "s" + String.valueOf(id);
			Device dev = Device.getByInternalId(session, internalId);
			
			if (dev == null) {
				dev = new TellStickThermometer(0.0);
				dev.setInternalId(internalId);
				session.save(dev);
			}
		}*/
		
		Hibernate.closeSession(session);
	}
}
