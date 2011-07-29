package com.homekey.core.main;

import com.homekey.core.Logs;
import com.homekey.core.log.L;

public class Main {
	public static void main(String[] args) {
		new ThreadMaster();
		L.setStandard("homekey");
		
		L.d("Här är en debug");
		L.i("Här är en info");
		L.w("Här är en varning!!!!");
		L.e("Här är EETTT FEEEEL!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		
		// DoSomeTesting(tm.getMonitor(),b);
		// tm.shutdown();
		// b.close();
	}
	
	// private static void DoSomeTesting(Monitor m, Database b) {
	// // Create devices
	// Device dev1 = new MockDeviceSwitcher("DA", "My MockDevice #1", true);
	// Device dev2 = new MockDeviceDimmer("DDD", "My MockDevice #2", true);
	// // Register devices with db
	// b.ensureDevice(dev1);
	// b.ensureDevice(dev2);
	//
	// b.putRow(dev1, new Object[] { Calendar.getInstance().getTime(), true });
	// b.putRow(dev1, new Object[] { Calendar.getInstance().getTime(), false });
	//
	// // Add devices
	// m.forceAddDevice(dev1);
	// m.forceAddDevice(dev2);
	// // Query devices by ID
	// Switchable switchable = (Switchable) m.getDevice(dev1.getId());
	// Dimmable dimmable = (Dimmable) m.getDevice(dev2.getId());
	// // Create commands
	// DimDeviceCommand ddc = new DimDeviceCommand(dimmable, 50);
	// SwitchDeviceCommand sdcOn = new SwitchDeviceCommand(switchable, true);
	// SwitchDeviceCommand sdcOff = new SwitchDeviceCommand(switchable, false);
	//
	// // Post commands
	// m.post(sdcOn);
	// m.post(ddc);
	// m.post(sdcOff);
	//
	// sdcOff.getResult();
	// ddc.getResult();
	// sdcOn.getResult();
	// }
}
