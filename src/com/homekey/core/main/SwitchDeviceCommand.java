package com.homekey.core.main;

import com.homekey.core.device.Device;
import com.homekey.core.device.Switchable;

public class SwitchDeviceCommand extends Command<Void> {
	
	private boolean on;
	private Switchable switcher;
	
	public SwitchDeviceCommand(Switchable switcher, boolean on) {
		super();
		this.switcher = switcher;
		this.on = on;
	}
	
	@Override
	public void run() {
		if (on)
			switcher.on();
		else
			switcher.off();
	}
}
