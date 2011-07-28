package com.homekey.core.main;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.UpdateDevicesCommand;
import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;
import com.homekey.core.storage.Database;

public class DetectorThread extends Thread {
	private CommandQueue queue;
	private Detector[] detectors;
	private Database db;
	
	public DetectorThread(CommandQueue queue, Database db) {
		this.queue = queue;
		this.db = db;
		this.detectors = new Detector[] { 
				//new OneWireDetector(), 
				new TellStickDetector() 
			};
	}
	
	@Override
	public void run() {
		while (true) {
			for (Detector d : detectors) {
				Device[] devs = d.findDevices();
				queue.post(new UpdateDevicesCommand(devs, db));
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
