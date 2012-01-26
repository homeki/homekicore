package com.homeki.core.device.tellstick;

import java.util.List;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.main.Monitor;

public class TellStickDetector extends ControlledThread {
	private Monitor monitor;
	private List<Integer> allowedSensorIds;
	
	public TellStickDetector(int interval, Monitor monitor, List<Integer> allowedSensorIds) {
		super(interval);
		this.monitor = monitor;
		this.allowedSensorIds = allowedSensorIds;
	}

	@Override
	protected void iteration() throws InterruptedException {
		int[] ids = TellStickNative.getDeviceIds();
		
		for (int i = 0; i < ids.length; i++) {
			int id = ids[i];
			String internalId = String.valueOf(id);
			
			if (!monitor.containsDevice(internalId)) {
				String type = TellStickNative.getDeviceType(id);
				
				if (type.equals("dimmer"))
					monitor.addDevice(new TellStickDimmer(internalId));
				else if (type.equals("switch"))
					monitor.addDevice(new TellStickSwitch(internalId));
				else
					L.w("Found no corresponding device for TellStick device type " + type + ".");
			}
		}
		
		for (int id : allowedSensorIds) {
			String internalId = "s" + String.valueOf(id);
			
			if (!monitor.containsDevice(internalId))
				monitor.addDevice(new TellStickThermometer(internalId));
		}
	}
}
