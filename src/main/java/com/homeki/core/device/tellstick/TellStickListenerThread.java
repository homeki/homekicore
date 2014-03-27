package com.homeki.core.device.tellstick;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.logging.L;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.storage.Hibernate;

public class TellStickListenerThread extends ControlledThread {
	public TellStickListenerThread() {
		super(0);
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		String raw = TellStickApi.INSTANCE.getEvent();
		String s[] = raw.split(" ");
		
		String type = s[0];
		String internalId = s[1];

		if (type.equals("sensor"))
			internalId = "s" + internalId;

		Session session = Hibernate.openSession();
		Device d = Device.getByInternalId(internalId);
		
		if (d == null) {
			if (type.equals("sensor")) {
				L.i("Found new TellStickThermometer with internal ID " + internalId + ".");
				d = new TellStickThermometer(Double.parseDouble(s[2]));
				d.setInternalId(internalId);
				session.save(d);
			} else {
				L.w("Got a TellStick native event with internal ID " + internalId + ", but found no corresponding device.");
			}
		} else {
			if (d instanceof TellStickSwitch) {
				boolean on = Boolean.parseBoolean(s[2]);
				d.addHistoryPoint(TellStickSwitch.ONOFF_CHANNEL, on ? 1 : 0);
				L.i("Received '" + on + "' from TellStickListener.");
			} else if (d instanceof TellStickDimmer) {
				try {
					int level = Integer.parseInt(s[2]);
					d.addHistoryPoint(TellStickDimmer.LEVEL_CHANNEL, level);
					L.i("Received '" + level + "' from TellStickListener.");
				} catch (NumberFormatException e) {
					boolean on = Boolean.parseBoolean(s[2]);
					d.addHistoryPoint(TellStickDimmer.ONOFF_CHANNEL, on ? 1 : 0);
					L.i("Received '" + on + "' from TellStickListener.");
				}
			} else if (d instanceof TellStickThermometer) {
				double value = Double.parseDouble(s[2]);
				d.addHistoryPoint(TellStickThermometer.TEMPERATURE_CHANNEL, value);
				L.i("Received '" + value + "'C from TellStickListener.");
			}
		}
		
		Hibernate.closeSession(session);
	}
}
