package com.homeki.core.device.onewire;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.main.Monitor;

public class OneWireDetector extends ControlledThread {
	private String path;
	private Monitor monitor;
	
	public OneWireDetector(int interval, String path, Monitor monitor) {
		super(interval);
		this.path = path;
		this.monitor = monitor;
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

	@Override
	protected void iteration() throws InterruptedException {
		List<String> ids = findInternalIds();
		
		if (ids == null)
			return;
		
		for (String s : ids) {
			String deviceDirPath = path + "/" + s;
			
			if (!monitor.containsDevice(s)) {
				String type = OneWireDevice.getStringVar(deviceDirPath, "type");
				
				if (type.equals("DS18S20") || type.equals("DS18B20")) {
					monitor.addDevice(new OneWireThermometer(s, deviceDirPath));
				} else {
					L.w("Found no corresponding device for 1-wire device type " + type + ".");
					continue;
				}
			}
		}
	}
}
