package com.homekey.core.main;

import com.homekey.core.device.IntervalLoggable;

public class CollectorThread extends Thread {
	
	private Monitor monitor;

	public CollectorThread(Monitor monitor) {
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
		while (true) {
			for (IntervalLoggable<?> il : monitor.getLoggableDevices()) {
				il.updateValue();
			}
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				System.out.println("CollectorThread interrupted.");
				return;
			}
		}
	}
}
