package com.homeki.core.device.tellstick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.main.Monitor;

public class TellStickDetector extends ControlledThread {
	private Monitor monitor;
	
	public TellStickDetector(int interval, Monitor monitor) {
		super(interval);
		this.monitor = monitor;
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
				else if (type.equals("fakedimmer"))
					monitor.addDevice(new TellStickFakeDimmer(internalId));
			}
		}
		
		
		File f = new File("sensors.list");
		if (f.exists()) {
			try {
				Scanner sc = new Scanner(f);
				while (sc.hasNext()) {
					String internalId = "s" + String.valueOf(sc.nextInt());
					
					if (!monitor.containsDevice(internalId))
						monitor.addDevice(new TellStickThermometer(internalId));
				}
			
			} catch (FileNotFoundException ex) {
				L.e("Error when adding TellStick devices from sensors.list.", ex);
			}
		}
	}
}
