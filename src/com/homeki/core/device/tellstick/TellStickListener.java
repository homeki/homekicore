package com.homeki.core.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Monitor;

public class TellStickListener extends ControlledThread {
	private final Monitor monitor;
	
	public TellStickListener(Monitor monitor) {
		super(0);
		this.monitor = monitor;
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		String raw = TellStickNative.getEvent();
		String s[] = raw.split(" ");
		
		String type = s[0];
		String 			internalId = s[1];

		if (type.equals("sensor")) {
			internalId = "s" + internalId;
		}

		Device d = monitor.getDevice(internalId);
		
		if (d instanceof TellStickSwitch) {
			boolean status = Boolean.parseBoolean(s[2]);
			((TellStickSwitch) d).setValue(status);
		} else if (d instanceof Dimmable) {
			int level = Integer.parseInt(s[2]);
			((TellStickDimmer) d).setValue(level);
		} else if (d instanceof TellStickThermometer) {
			double value = Double.parseDouble(s[2]);
			((TellStickThermometer) d).setValue(value);
		}
	}
}
