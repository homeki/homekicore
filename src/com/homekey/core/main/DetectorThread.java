package com.homekey.core.main;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;

public class DetectorThread extends Thread {
	private Monitor monitor;
	private Detector[] detectors;
	
	public DetectorThread(Monitor monitor) {
		this.monitor = monitor;
		this.detectors = new Detector[] { 
				new OneWireDetector(), 
				new TellStickDetector() 
			};
	}
	
	@Override
	public void run() {
		while (true) {
			for (Detector d : detectors) {
				Device[] devs = d.findDevices();
				
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println("DetectorThread interrupted.");
				return;
			}
		}
	}
}
