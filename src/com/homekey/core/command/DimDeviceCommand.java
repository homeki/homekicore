package com.homekey.core.command;

import com.homekey.core.device.Dimmable;

public class DimDeviceCommand extends Command<Void> {
	
	private int level;
	private Dimmable dimmable;

	public DimDeviceCommand(Dimmable dimmable, int level){
		this.dimmable = dimmable;
		this.level = level;
	}
	
	@Override
	public void run() {
		this.dimmable.dim(this.level);
		finish();
	}
}
