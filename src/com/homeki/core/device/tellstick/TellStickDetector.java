package com.homeki.core.device.tellstick;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.homeki.core.device.Detector;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.DeviceInformation.DeviceType;

public class TellStickDetector extends Detector {
	@Override
	public List<DeviceInformation> findDevices() {
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		int[] ids = TellStickNative.getDeviceIds();
		
		for (int i = 0; i < ids.length; i++) {
			int id = ids[i];
			DeviceInformation di = null;
			String type = TellStickNative.getDeviceType(id);
			
			if (type.equals("dimmer"))
				di = new DeviceInformation(String.valueOf(id), DeviceType.TellStickDimmer);
			else if (type.equals("switch"))
				di = new DeviceInformation(String.valueOf(id), DeviceType.TellStickSwitch);
			else if (type.equals("fakedimmer"))
				di = new DeviceInformation(String.valueOf(id), DeviceType.TellStickFakeDimmer);
			if (di != null)
				devices.add(di);
		}
		
		File f = new File("sensors.list");
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				int internalId = sc.nextInt();
				DeviceInformation di = new DeviceInformation("s"+String.valueOf(internalId), DeviceType.TellStickThermometer);
				devices.add(di);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return devices;
	}
}
