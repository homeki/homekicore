package com.homeki.core.device.tellstick;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.homeki.core.device.Detector;
import com.homeki.core.device.DeviceInformation;
import com.homeki.core.log.L;
import com.homeki.core.main.Util;

public class TellStickDetector extends Detector {
	private static final String valueFinder = "%s\\s*?=\\s*?(\\w+|\".*?\")";
	private static final String deviceFinder = "device?\\s*\\{(.*?)parameters";
	
	private String path;
	
	public TellStickDetector(String path) {
		this.path = path;
	}
	
	@Override
	public List<DeviceInformation> findDevices() {
		List<DeviceInformation> devices = new ArrayList<DeviceInformation>();
		
		Pattern idFinder = Pattern.compile(String.format(valueFinder, "id"));
		Pattern modelFinder = Pattern.compile(String.format(valueFinder, "model"));
		
		Pattern p = Pattern.compile(deviceFinder, Pattern.DOTALL);
		Matcher m = p.matcher(Util.fromFile(path));
		
		while (m.find()) {
			String match = m.group(1);
			String id = getMatch(match, idFinder);
			String model = getMatch(match, modelFinder);
			if (model.equals("\"selflearning-switch\"")) {
				devices.add(new DeviceInformation(id, TellStickSwitch.class));
			} else if (model.equals("\"selflearning-dimmer\"")) {
				devices.add(new DeviceInformation(id, TellStickDimmer.class));
			} else {
				L.w("TellStickDetector found unknown device.");
			}
		}
		
		return devices;
	}
	
	private String getMatch(String string, Pattern p) {
		Matcher m = p.matcher(string);
		if (m.find())
			return m.group(1);
		return null;
	}
}
