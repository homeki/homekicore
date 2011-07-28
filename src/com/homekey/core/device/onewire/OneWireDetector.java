package com.homekey.core.device.onewire;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;

public class OneWireDetector extends Detector {
	private final String OWFS_MOUNT_POINT = "/mnt/1wire/";
	private final String SENSOR_ROOT = "uncached/";
	
	public OneWireDetector() {
		
	}
	
	private String[] findSensorDirs() {
		List<String> dirList = new ArrayList<String>();
		File root = new File(OWFS_MOUNT_POINT + SENSOR_ROOT);
		
		String[] items = root.list();
		
		for (String s : items) {
			Pattern p = Pattern.compile("[0-9A-F]{2}\\.[0-9A-F]{12}");
			Matcher m = p.matcher(s);
			if (m.find()) {
				dirList.add(m.group());
			}
		}
		
		return dirList.toArray(null);
	}
	
	@Override
	public Device[] findDevices() {
		// TODO: add check if owfs is running, and if not, run it
		
		String[] sensorDirs = findSensorDirs();
		
		
		return null;
	}
}
