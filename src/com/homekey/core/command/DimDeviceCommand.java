package com.homekey.core.command;

import com.homekey.core.device.Dimmable;
import com.homekey.core.main.InternalData;

public class DimDeviceCommand extends Command<Boolean> {
	
	private int level;
	private Dimmable dimmable;

	public DimDeviceCommand(Dimmable dimmable, int level){
		this.dimmable = dimmable;
		this.level = level;
	}

	@Override
	public void internalRun(InternalData data) {
		this.dimmable.dim(this.level);
		setResult(true);
	}
	
	public int getLevel(){
		return level;
	}
}
