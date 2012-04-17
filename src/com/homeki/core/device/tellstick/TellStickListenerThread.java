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
		String raw = TellStickNative.getEvent();
		String s[] = raw.split(" ");
		
		String type = s[0];
		String internalId = s[1];

		if (type.equals("sensor"))
			internalId = "s" + internalId;

		Session session = Hibernate.openSession();
		Device d = Device.getByInternalId(internalId);
		
		if (d != null) {
			if (d instanceof TellStickSwitch) {
				boolean status = Boolean.parseBoolean(s[2]);
				((TellStickSwitch)d).addOnOffHistoryPoint(status);
				L.i("Received '" + status + "' from TellStickListener.");
			} else if (d instanceof TellStickDimmer) {
				// TODO: fix this! very temporary solution!
				// the reason we catch this exception is that we get a boolean
				// value of we do tdTurnOff() on a dimmer (and not a zero value)
				// could probably be handled more... elegant
				try {
					int level = Integer.parseInt(s[2]);
					((TellStickDimmer)d).addOnOffHistoryPoint(true);
					((TellStickDimmer)d).addLevelHistoryPoint(level);
					L.i("Received '" + level + "' from TellStickListener.");
				} catch (NumberFormatException e) {
					((TellStickDimmer)d).addOnOffHistoryPoint(false);
					L.i("Received 'NumberFormatException' from TellStickListener.");
				}
			} else if (d instanceof TellStickThermometer) {
				double value = Double.parseDouble(s[2]);
				((TellStickThermometer)d).addHistoryPoint(value);
				L.i("Received '" + value + "'C from TellStickListener.");
			}
		} else if (type.equals("sensor")){
			L.i("Register sensor value from " + internalId + ".");
			d = new TellStickThermometer(Double.parseDouble(s[2]));
			d.setInternalId(internalId);
			session.save(d);
		}
		
		Hibernate.closeSession(session);
	}
}
