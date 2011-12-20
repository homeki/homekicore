package com.homeki.core.threads;

import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.main.Monitor;

public class CollectorThread extends ControlledThread {
	private Monitor monitor;

	public CollectorThread(Monitor monitor) {
		super(1000);
		this.monitor = monitor;
	}

	@Override
	public void iteration() throws InterruptedException {
		for (IntervalLoggable<?> il : monitor.getLoggableDevices()) {
			il.updateValue();
		}
	}
}
