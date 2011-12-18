package com.homeki.core.device.tellstick;

import java.util.ArrayList;
import java.util.List;

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
		
		return devices;
	}
}
