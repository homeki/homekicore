package com.homeki.core.device.tellstick;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.storage.Hibernate;

public class TellStickListener extends ControlledThread {
	public TellStickListener() {
		super(0);
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		String raw = TellStickNative.getEvent();
		String s[] = raw.split(" ");
		
		String type = s[0];
		String internalId = s[1];

		if (type.equals("sensor")) {
			internalId = "s" + internalId;
		}

		Session session = Hibernate.openSession();
		Device d = Device.getByInternalId(session, internalId);
		
		if (d != null) {
			if (d instanceof TellStickSwitch) {
				boolean status = Boolean.parseBoolean(s[2]);
				((TellStickSwitch)d).addHistoryValue(status);
			} else if (d instanceof TellStickDimmer) {
				int level = Integer.parseInt(s[2]);
				((TellStickDimmer)d).addHistoryValue(level);
			} else if (d instanceof TellStickThermometer) {
				double value = Double.parseDouble(s[2]);
				((TellStickThermometer)d).addHistoryValue(value);
			}
			
			session.save(d);
		}
		
		Hibernate.closeSession(session);
	}
}
