package com.homekey.core.command;

import com.homekey.core.device.Switchable;

public class SwitchDeviceCommand extends Command<Void> {
	
	private boolean on;
	private Switchable switcher;
	
	public SwitchDeviceCommand(Switchable switcher, boolean on) {
		super();
		if (switcher == null){
			throw new NullPointerException();
		}
		this.switcher = switcher;
		this.on = on;
	}
	
	@Override
	public void internalRun() {
		if (on)
			switcher.on();
		else
			switcher.off();
		finish();
	}
	
}
