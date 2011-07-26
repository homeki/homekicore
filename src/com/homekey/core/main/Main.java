package com.homekey.core.main;

import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Switchable;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.device.mock.MockDeviceSwitcher;
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
		
		DoSomeTesting(m, ct);
		DoSomeMoreTesting(b);
		
		b.close();
	}

	private static void DoSomeTesting(Monitor m, CommandsThread ct) {
		// Add devices
		m.forceAddDevice(new MockDeviceSwitcher(1, "My MockDevice #1", true));
		m.forceAddDevice(new MockDeviceDimmer(2, "My MockDevice #2", true));
		// Get devices
		Switchable dev1 = (Switchable)m.getDevice(1);
		Dimmable dev2 = (Dimmable)m.getDevice(2);
		// Create commands
		DimDeviceCommand ddc = new DimDeviceCommand(dev2,50);
		SwitchDeviceCommand sdcOn = new SwitchDeviceCommand(dev1,true);
		SwitchDeviceCommand sdcOff = new SwitchDeviceCommand(dev1,false);
		// Post commands
		ct.post(sdcOn);
		ct.post(ddc);
		ct.post(sdcOff);
		
		sdcOff.getResult();
		ddc.getResult();
		sdcOn.getResult();
	}
	
	private static void DoSomeMoreTesting(Database b) {
		MockDeviceSwitcher sw = new MockDeviceSwitcher(2, "test", false);
		
		b.createTableFor(sw);
		
		
	}
}
