package com.homeki.core.device.camera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.DeviceInformation.DeviceType;

public class CameraDetector extends Detector {
	private static final int MAX_WEBCAMS = 5;

	@Override
	public List<DeviceInformation> findDevices() {
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		for (int i = 0; i < MAX_WEBCAMS; i++) {
			String path = "/dev/video"+i;
			File f = new File(path);
			if(f.canRead()){
				DeviceInformation di = new DeviceInformation(path, DeviceType.Camera);
				di.addAdditionalData("nick", "Camera #"+ (i+1));
				devices.add(di);
			}
		}
		return devices;
	}

}
