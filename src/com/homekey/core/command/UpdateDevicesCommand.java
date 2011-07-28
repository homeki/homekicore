package com.homekey.core.command;

import java.util.List;

import com.homekey.core.device.Device;
import com.homekey.core.main.InternalData;
import com.homekey.core.storage.Database;

public class UpdateDevicesCommand extends Command<Void> {
	private List<Device> devices;
	private Database db;
	
	public UpdateDevicesCommand(List<Device> devices, Database db) {
		this.devices = devices;
		this.db = db;
	}
	
	@Override
	public void internalRun(InternalData data) {
		for (Device d : devices) {
			if (!data.containsDevice(d)) {
				db.ensureDevice(d);
				data.addDevice(d);
			}
		}
	}
}
