package com.homeki.core.device.tellstick;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.main.L;
import com.homeki.core.main.Monitor;
import com.homeki.core.threads.ControlledThread;

public class TellStickListener extends ControlledThread {
	private final Monitor monitor;
	
	public TellStickListener(Monitor monitor) {
		super(0);
		this.monitor = monitor;
	}
	
	@Override
	protected void iteration() throws InterruptedException {
		String s[] = TellStickNative.getEvent().split(" ");
		
		int id = Integer.parseInt(s[0]);
		Device d = monitor.getDevice(id);
		
		if (d instanceof TellStickSwitch) {
			boolean status = Boolean.parseBoolean(s[1]);
			((TellStickSwitch) d).setValue(status);
		} else if (d instanceof Dimmable) {
			int level = Integer.parseInt(s[0]);
			((TellStickDimmer) d).setValue(level);
		}
	}
	
}
