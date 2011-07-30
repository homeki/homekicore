package com.homekey.core.main;

import java.util.List;

import com.homekey.core.device.Detector;
import com.homekey.core.device.DeviceFactory;
import com.homekey.core.device.DeviceInformation;
import com.homekey.core.device.mock.MockDetector;
import com.homekey.core.device.onewire.OneWireDetector;
import com.homekey.core.device.tellstick.TellStickDetector;
import com.homekey.core.storage.Database;

public class DetectorThread extends ControlledThread {
	private Detector[] detectors;
	private Monitor monitor;
	private Database db;
	
	public DetectorThread(Monitor monitor, Database db) {
		super(10000);
		this.db = db;
		this.monitor = monitor;
		this.detectors = new Detector[] { 
				new MockDetector(),
				new OneWireDetector("/mnt/1wire/uncached"),
				new TellStickDetector("/etc/tellstick.conf") //TODO: put in better place
			};
	}

	@Override
	public void iteration() throws InterruptedException {
		for (Detector det : detectors) {
			List<DeviceInformation> devs = det.findDevices();
			
			if (devs != null) {
				for (DeviceInformation d : devs) {
					if (!monitor.containsDevice(d.getInternalId())) {
						monitor.addDevice(DeviceFactory.createDevice(db, d));
					}
				}
			}
		}
	}
}
