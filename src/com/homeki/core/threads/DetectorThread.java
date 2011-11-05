package com.homeki.core.threads;

import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.Device;
import com.homeki.core.device.DeviceFactory;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.camera.CameraDetector;
import com.homeki.core.device.mock.MockDetector;
import com.homeki.core.main.Monitor;
import com.homeki.core.storage.ITableFactory;

public class DetectorThread extends ControlledThread {
	private Detector[] detectors;
	private Monitor monitor;
	private ITableFactory dbf;
	
	public DetectorThread(Monitor monitor, ITableFactory dbf) {
		super(1000);
		this.dbf = dbf;
		this.monitor = monitor;
		this.detectors = new Detector[] { 
				/*new CameraDetector()*/
				new MockDetector()
				//new OneWireDetector("/mnt/1wire/uncached"),
				/*new TellStickDetector("/etc/tellstick.conf")*/
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
