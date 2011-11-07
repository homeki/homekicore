package com.homeki.core.threads;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.Device;
import com.homeki.core.device.DeviceFactory;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.camera.CameraDetector;
import com.homeki.core.device.mock.MockDetector;
import com.homeki.core.device.onewire.OneWireDetector;
import com.homeki.core.device.tellstick.TellStickDetector;
import com.homeki.core.log.L;
import com.homeki.core.main.Configurable;
import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.Monitor;
import com.homeki.core.storage.ITableFactory;

public class DetectorThread extends ControlledThread implements Configurable {
	private List<Detector> detectors;
	private Monitor monitor;
	private ITableFactory dbf;
	
	public DetectorThread(Monitor monitor, ITableFactory dbf) {
		super(1000);
		this.dbf = dbf;
		this.monitor = monitor;
		this.detectors = new ArrayList<Detector>();
	}
	
	@Override
	public void configure(ConfigurationFile file) {
		if (file.getBool("detector.mock.use")) {
			detectors.add(new MockDetector());
		}
		if (file.getBool("detector.tellstick.use")) {
			detectors.add(new TellStickDetector(file.getString("detector.tellstick.path")));
		}
		if (file.getBool("detector.onewire.use")) {
			detectors.add(new OneWireDetector(file.getString("detector.onewire.path")));
		}
		if (file.getBool("detector.camera.use")) {
			detectors.add(new CameraDetector());
		}	
		if (detectors.size() == 0) {
			L.w("No detectors specified, no new devices will be found.");
		}
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
							System.out.println("adding: " +dev.toString());
						}
					}
				}
			}
		}
	}
}
