package com.homekey.core.command;

import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.main.InternalData;

public class DimDeviceCommand extends Command<Boolean> {
	
	private int level;
	private int id;

	public DimDeviceCommand(int id, int level){
		this.id = id;
		this.level = level;
	}

	@Override
	public void internalRun(InternalData data) {
		Device dev = data.getDevice(id);
		Dimmable dim = (Dimmable)dev;
		dim.dim(this.level);
		setResult(true);
	}
	
	public int getLevel(){
		return level;
	}
}
