package com.homekey.core.threads;

import java.util.List;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;
import com.homekey.core.device.DeviceFactory;
import com.homekey.core.device.DeviceInformation;
import com.homekey.core.device.mock.MockDetector;
import com.homekey.core.main.Monitor;
import com.homekey.core.storage.ITableFactory;

public class DetectorThread extends ControlledThread {
	private Detector[] detectors;
	private Monitor monitor;
	private ITableFactory dbf;
	
	public DetectorThread(Monitor monitor, ITableFactory dbf) {
		super(10000);
		this.dbf = dbf;
		this.monitor = monitor;
		this.detectors = new Detector[] { 
				new MockDetector(),
				//new OneWireDetector("/mnt/1wire/uncached"),
				//new TellStickDetector("/etc/tellstick.conf")
			};
	}

	@Override
	public void iteration() throws InterruptedException {
		for (Detector det : detectors) {
			List<DeviceInformation> devs = det.findDevices();

			if (devs != null) {
				for (DeviceInformation d : devs) {
					if (!monitor.containsDevice(d.getInternalId())) {
						Device dev = DeviceFactory.createDevice(dbf, d);
						if (dev != null) {
							monitor.addDevice(dev);
						}
					}
				}
			}
		}
	}
}
