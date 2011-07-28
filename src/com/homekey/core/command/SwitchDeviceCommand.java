package com.homekey.core.command;

import com.homekey.core.device.Device;
import com.homekey.core.device.Switchable;
import com.homekey.core.main.InternalData;

public class SwitchDeviceCommand extends Command<Boolean> {
	
	private boolean on;
	private int id;
	
	public SwitchDeviceCommand(int id, boolean on) {
		this.id = id;
		this.on = on;
	}
	
	@Override
	public void internalRun(InternalData data) {
		Device dev = data.getDevice(id);
		if (dev == null) {
			
		} else {
			Switchable switcher = (Switchable) dev;
			if (on)
				switcher.on();
			else
				switcher.off();
		}
		setResult(true);
	}
	
	public boolean turningOn() {
		return on;
	}
}
