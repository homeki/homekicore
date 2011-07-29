package com.homekey.core.main;

import java.util.List;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDetector;
import com.homekey.core.device.onewire.OneWireDetector;
import com.homekey.core.device.tellstick.TellStickDetector;
import com.homekey.core.log.L;

public class DetectorThread extends ControlledThread {
	private Detector[] detectors;
	private Monitor monitor;
	
	public DetectorThread(Monitor monitor) {
		super(10000);
		L.d("Creating detector thread..");
		this.monitor = monitor;
		this.detectors = new Detector[] { 
				new MockDetector(),
				new OneWireDetector(),
				new TellStickDetector("/etc/tellstick.conf") //TODO: put in better place
			};
	}

	@Override
	public void iteration() throws InterruptedException {
		for (Detector det : detectors) {
			List<Device> devs = det.findDevices();
			
			if (devs != null) {
				for (Device d : devs) {
					if (!monitor.containsDevice(d)) {
						monitor.addDevice(d);
					}
				}
			}
		}
	}
}
