package com.homekey.core.threads;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.main.Monitor;

public class CollectorThread extends ControlledThread {
	private Monitor monitor;

	public CollectorThread(Monitor monitor) {
		super(10000);
		this.monitor = monitor;
	}

	@Override
	public void iteration() throws InterruptedException {
		for (IntervalLoggable<?> il : monitor.getLoggableDevices()) {
			il.updateValue();
		}
	}
}
