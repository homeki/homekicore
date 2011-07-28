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
	
	private String[] findSensor() {
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
	
	public List<Device> findDevices() {
		// TODO: add check if owfs is running, and if not, run it
		
		String[] sensors = findSensor();
		List<Device> devices = new ArrayList<Device>();
		
		for (String s : sensors) {
			String typeFilePath = OWFS_MOUNT_POINT + SENSOR_ROOT + s + "/type";
			File typeFile = new File(typeFilePath);
			Scanner typeScanner;
			
			try {
				typeScanner = new Scanner(typeFile);
			} catch (FileNotFoundException ex) {
				System.err.println("OneWireDetector should have found type file, didn't.");
				continue;
			}
			
			String type = typeScanner.next();
			Device device;
			
			if (type.equals("DS18S20")) {
				device = new OneWireTemperatureSensor(s, OWFS_MOUNT_POINT + SENSOR_ROOT + s);
				devices.add(device);
			}
			else {
				System.err.println("OneWireDetector didn't understand device type " + type);
			}
		}
		
		return devices;
	}
}
