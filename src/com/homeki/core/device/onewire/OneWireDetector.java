package com.homeki.core.device.onewire;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.storage.Hibernate;

public class OneWireDetector extends ControlledThread {
	private final static String DETECTOR = "detector";
	
	private Set<String> loggedSet;
	
	public OneWireDetector() {
		super(Configuration.ONEWIRE_DETECTOR_INTERVAL);
		loggedSet = new HashSet<String>();
	}
	
	private List<String> findInternalIds() {
		List<String> dirList = new ArrayList<String>();
		File root = new File(Configuration.ONEWIRE_PATH);
		
		String[] items = root.list();

		if (items == null && loggedSet.add(DETECTOR)) {
			L.e("1-wire network not found. Detection of devices failed.");
		} else { 
			for (String s : items) {
				Pattern p = Pattern.compile("[0-9A-F]{2}\\.[0-9A-F]{12}");
				Matcher m = p.matcher(s);
				if (m.find()) {
					dirList.add(m.group());
				}
			}
		
			if (loggedSet.remove(DETECTOR))
				L.i("1-wire network found again. Detection of devices succeeded.");
		}
		
		return dirList;
	}

	@Override
	protected void iteration() throws InterruptedException {
		List<String> ids = findInternalIds();
		Session session = Hibernate.openSession();
		
		for (String s : ids) {
			String deviceDirPath = Configuration.ONEWIRE_PATH + "/" + s;
			Device dev = Device.getByInternalId(session, s);
			
			if (dev == null) {
				String type = OneWireDevice.getStringVar(deviceDirPath, "type");
				
				if (type.equals("DS18S20") || type.equals("DS18B20")) {
					dev = new OneWireThermometer(0.0);
					dev.setInternalId(s);
					session.save(dev);
				} else if (loggedSet.add(s)) {
					L.e("Found no corresponding device for 1-wire device with internal id " + s + " and type " + type + ".");
				}
			}
		}
		
		Hibernate.closeSession(session);
	}
}
