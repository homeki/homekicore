package com.homeki.core.threads;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.Device;
import com.homeki.core.device.DeviceFactory;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.main.Monitor;
import com.homeki.core.storage.ITableFactory;

public class DetectorThread extends ControlledThread {
	private List<Detector> detectors;
	private Monitor monitor;
	private ITableFactory dbf;
	
	public DetectorThread(List<Detector> detectors, Monitor monitor, ITableFactory dbf) {
		super(10000);
		this.dbf = dbf;
		this.monitor = monitor;
		this.detectors = detectors;
	}

	@Override
	public void iteration() throws InterruptedException {
		for (Detector det : detectors) {
			List<DeviceInformation> devs = det.findDevices();

			if (devs != null) {
				for (DeviceInformation d : devs) {
					if (!monitor.containsDevice(d.getInternalId())) {
						Device dev = DeviceFactory.createDevice(dbf, d);
						if (dev != null)
							monitor.addDevice(dev);
					}
				}
			}
		}
	}
}
