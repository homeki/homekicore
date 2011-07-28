package com.homekey.core.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.homekey.core.storage.Database;
import com.homekey.core.storage.impl.SqliteDatabase;

public class Main {
	public static void main(String[] args) {
		//ThreadMaster tm = new ThreadMaster();
		
		//Database b = new SqliteDatabase();
		
		String s = "81.D44F29000000";
		//Pattern p = Pattern.compile("[A-F/d]{2}\\.[A-F/d]{12}");
		Pattern p = Pattern.compile("[0-9A-F]{2}\\.[0-9A-F]");
		Matcher m = p.matcher(s);
		while (m.find()) {
			System.out.println(m.group());
		}
		
		//DoSomeTesting(tm.getMonitor(),b);
//		tm.shutdown();
//		b.close();
	}
	
//	private static void DoSomeTesting(Monitor m,  Database b) {
//		// Create devices
//		Device dev1 = new MockDeviceSwitcher("DA", "My MockDevice #1", true);
//		Device dev2 = new MockDeviceDimmer("DDD", "My MockDevice #2", true);
//		// Register devices with db
//		b.ensureDevice(dev1);
//		b.ensureDevice(dev2);
//		
//		b.putRow(dev1, new Object[] { Calendar.getInstance().getTime(), true });
//		b.putRow(dev1, new Object[] { Calendar.getInstance().getTime(), false });
//		
//		// Add devices
//		m.forceAddDevice(dev1);
//		m.forceAddDevice(dev2);
//		// Query devices by ID
//		Switchable switchable = (Switchable) m.getDevice(dev1.getId());
//		Dimmable dimmable = (Dimmable) m.getDevice(dev2.getId());
//		// Create commands
//		DimDeviceCommand ddc = new DimDeviceCommand(dimmable, 50);
//		SwitchDeviceCommand sdcOn = new SwitchDeviceCommand(switchable, true);
//		SwitchDeviceCommand sdcOff = new SwitchDeviceCommand(switchable, false);
//		
//		// Post commands
//		m.post(sdcOn);
//		m.post(ddc);
//		m.post(sdcOff);
//		
//		sdcOff.getResult();
//		ddc.getResult();
//		sdcOn.getResult();
//	}
}
