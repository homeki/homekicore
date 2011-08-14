package com.homekey.core.main;

import com.homekey.core.device.IntervalLoggable;

public class CollectorThread extends ControlledThread {
	private Monitor monitor;

	public CollectorThread(Monitor monitor) {
		super(5000);
		this.monitor = monitor;
	}

	@Override
	public void iteration() throws InterruptedException {
		for (IntervalLoggable<?> il : monitor.getLoggableDevices()) {
			il.updateValue();
		}
	}
}
