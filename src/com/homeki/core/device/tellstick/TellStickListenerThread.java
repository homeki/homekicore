package com.homeki.core.device.tellstick;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class TellStickListenerThread extends ControlledThread {
	public TellStickListenerThread() {
		super(0);
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		L.i("Before getevent");
		String raw = TellStickNative.getEvent();
		L.i("After getevent. raw: " + raw);
		String s[] = raw.split(" ");
		
		String type = s[0];
		String internalId = s[1];

		if (type.equals("sensor")) {
			internalId = "s" + internalId;
		}

		L.i("Opening session");
		Session session = Hibernate.openSession();
		L.i("session open!");
		Device d = Device.getByInternalId(session, internalId);
		L.i("got device! " + d);
		
		if (d != null) {
			L.i("d is not null!");
			if (d instanceof TellStickSwitch) {
				boolean status = Boolean.parseBoolean(s[2]);
				L.i("adding history point (switch)");
				((TellStickSwitch)d).addHistoryPoint(status);
				L.i("Received '" + status + "' from TellStickListener.");
			} else if (d instanceof TellStickDimmer) {
				// TODO: fix this! very temporary solution!
				// the reason we catch this exception is that we get a boolean
				// value of we do tdTurnOff() on a dimmer (and not a zero value)
				// could probably be handled more... elegant
				try {
					int level = Integer.parseInt(s[2]);
					int value = 1;
					L.i("adding history point (dimmer)");
					((TellStickDimmer)d).addHistoryPoint(value, level);
					L.i("Received '" + level + "' from TellStickListener.");
				} catch (NumberFormatException e) {
					int value = 0;
					((TellStickDimmer)d).addHistoryPoint(value, session);
					L.i("Received 'NumberFormatException' from TellStickListener.");
				}
			} else if (d instanceof TellStickThermometer) {
				double value = Double.parseDouble(s[2]);
				L.i("adding history point (termo)");
				((TellStickThermometer)d).addHistoryPoint(value);
				L.i("Received '" + value + "'C from TellStickListener.");
			}
		} else if (type.equals("sensor")){
			L.i("Register sensor " + internalId);
			d = new TellStickThermometer(Double.parseDouble(s[2]));
			d.setInternalId(internalId);
			session.save(d);
		}
		L.i("before close");
		Hibernate.closeSession(session);
		L.i("after close");
	}
}
