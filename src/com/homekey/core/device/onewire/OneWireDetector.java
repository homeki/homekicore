package com.homekey.core.device.onewire;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.homekey.core.device.Detector;
import com.homekey.core.device.DeviceInformation;
import com.homekey.core.log.L;

public class OneWireDetector extends Detector {
	private String path;
	
	public OneWireDetector(String path) {
		this.path = path;
	}
	
	private List<String> findInternalIds() {
		List<String> dirList = new ArrayList<String>();
		File root = new File(path);
		
		String[] items = root.list();

		if (items == null) {
			L.w("1-wire network not found. Detection of devices failed.");
			return null;
		}

		if (items != null){
			for (String s : items) {
				Pattern p = Pattern.compile("[0-9A-F]{2}\\.[0-9A-F]{12}");
				Matcher m = p.matcher(s);
				if (m.find()) {
					dirList.add(m.group());
				}
			}
		}
		
		return dirList;
	}
	
	public List<DeviceInformation> findDevices() {
		// TODO: add check if owfs is running, and if not, run it
		
		List<String> sensors = findInternalIds();
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		
		if (sensors == null) {
			return null;
		}
		
		for (String s : sensors) {
			String deviceDirPath = path + "/" + s;
			DeviceInformation di;
			String type = OneWireDevice.getStringVar(deviceDirPath, "type");
			
			if (type.equals("DS18S20")) {
				di = new DeviceInformation(s, OneWireTemperatureDevice.class);
				di.addAdditionalData("deviceDirPath", deviceDirPath);
				devices.add(di);
			} else {
				L.w("Found no corresponding device for 1-wire device type " + type + ".");
				continue;
			}
		}
		
		return devices;
	}
}
