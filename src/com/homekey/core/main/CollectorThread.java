package com.homekey.core.main;

import com.homekey.core.device.IntervalLoggable;
import com.homekey.core.log.L;

public class CollectorThread extends ControlledThread {
	
	private Monitor monitor;

	public CollectorThread(Monitor monitor) {
		super(15000);
		this.monitor = monitor;
	}

	@Override
	public void iteration() throws InterruptedException {
		for (IntervalLoggable<?> il : monitor.getLoggableDevices()) {
			il.updateValue();
		}
	}
}
