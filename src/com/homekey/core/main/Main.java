package com.homekey.core.main;

import com.homekey.core.device.Dimmable;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.storage.Database;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Database b = new Database();
		Monitor m = new Monitor();
		CommandsThread ct = new CommandsThread();
		ct.start();
		
		m.setServerName("Fresh Server");

		
		System.out.println("Starting server '" + m.getServerName() + "'");
		
		// Test
		m.forceAddDevice(new MockDeviceDimmer(1, "My MockDevice #1", true));
		m.forceAddDevice(new MockDeviceDimmer(2, "My MockDevice #2", true));
		Dimmable dev = (Dimmable)m.getDevice(2);
		
		DimDeviceCommand ddc = new DimDeviceCommand(dev,50);
		ct.post(ddc);
		
		ddc.getResult();
		
	}
}
