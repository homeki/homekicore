package com.homeki.core.device.onewire;

import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.IntervalLoggable;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Monitor;

public class OneWireCollector extends ControlledThread {
	private Monitor monitor;
	
	public OneWireCollector(int interval, Monitor monitor) {
		super(interval);
		this.monitor = monitor;
	}

	@Override
	protected void iteration() throws InterruptedException {
		List<Device> devices = monitor.getDevices();
		
		for (Device d : devices) {
			if (d instanceof OneWireThermometer && d instanceof IntervalLoggable<?>) {
				((IntervalLoggable<?>)d).updateValue();
			}
		}
	}
}
