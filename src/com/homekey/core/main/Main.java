package com.homekey.core.main;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.command.DimDeviceCommand;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Switchable;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.http.HttpListenerThread;
import com.homekey.core.device.mock.MockDeviceSwitcher;
import com.homekey.core.storage.Database;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadMaster tm = new ThreadMaster();
		Database b = new Database();
		
		DoSomeTesting( tm.getMonitor(), tm.getCommandThread(),b);
		DoSomeMoreTesting(b);
	}
	
	private static void DoSomeTesting(Monitor m, CommandsThread ct, Database b) {
		// Create devices
		Device dev1 = new MockDeviceSwitcher(b.getNextId(), "DA", "My MockDevice #1", true);
		b.registerDevice(dev1);
		Device dev2 = new MockDeviceDimmer(b.getNextId(), "DDD", "My MockDevice #2", true);
		b.registerDevice(dev2);
		// Add devices
		m.forceAddDevice(dev1);
		m.forceAddDevice(dev2);
		// Query devices by ID
		Switchable switchable = (Switchable) m.getDevice(dev1.getId());
		Dimmable dimmable = (Dimmable) m.getDevice(dev2.getId());
		// Create commands
		DimDeviceCommand ddc = new DimDeviceCommand(dimmable, 50);
		SwitchDeviceCommand sdcOn = new SwitchDeviceCommand(switchable, true);
		SwitchDeviceCommand sdcOff = new SwitchDeviceCommand(switchable, false);
		// Post commands
		ct.post(sdcOn);
		ct.post(ddc);
		ct.post(sdcOff);
		
		sdcOff.getResult();
		ddc.getResult();
		sdcOn.getResult();
	}
	
	private static void DoSomeMoreTesting(Database b) {
		MockDeviceSwitcher sw = new MockDeviceSwitcher(2, "ID123", "test", false);
		
		if (!b.deviceExists(sw)) {
			b.registerDevice(sw);
		}
		
		System.out.println("NextID: " + b.getNextId());
	}
}
