package com.homeki.core.device.onewire;

import com.homeki.core.device.Device;
import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneWireDetectorThread extends ControlledThread {
	private final static String DETECTOR_LOG_DIFF = "detector";
	private final static String TYPE_LOG_DIFF = "type/";
	private final static String NOCORR_LOG_DIFF = "nocorr/";
	
	private Set<String> loggedSet;
	
	public OneWireDetectorThread() {
		super(Configuration.ONEWIRE_DETECTOR_INTERVAL);
		loggedSet = new HashSet<>();
	}
	
	private List<String> findInternalIds() {
		List<String> dirList = new ArrayList<>();
		File root = new File(Configuration.ONEWIRE_PATH);
		
		String[] items = root.list();

		if (items == null) {
			if (loggedSet.add(DETECTOR_LOG_DIFF))
				L.e("1-wire mount point " + Configuration.ONEWIRE_PATH + " not found. Detection of devices failed. Log message throttled until next success.");
		} else { 
			for (String s : items) {
				Pattern p = Pattern.compile("[0-9A-F]{2}\\.[0-9A-F]{12}");
				Matcher m = p.matcher(s);
				if (m.find()) {
					dirList.add(m.group());
				}
			}
		
			if (loggedSet.remove(DETECTOR_LOG_DIFF))
				L.i("1-wire mount point found again. Detection of devices succeeded.");
		}
		
		return dirList;
	}

	@Override
	protected void iteration() throws InterruptedException {
		List<String> ids = findInternalIds();
		Session session = Hibernate.openSession();
		
		// loop through 1-wire network; if Homeki OneWireDevice is not found, create it
		for (String s : ids) {
			String deviceDirPath = Configuration.ONEWIRE_PATH + "/" + s;
			Device dev = Device.getByInternalId(s);
			
			if (dev == null) {
				String type;
				
				try {
					type = OneWireDevice.getStringVar(deviceDirPath, "type");
					if (loggedSet.remove(TYPE_LOG_DIFF + s))
						L.i("Succeeded retrieving device type for 1-wire device, had earlier failed.");
				} catch (Exception e) {
					if (loggedSet.add(TYPE_LOG_DIFF + s))
						L.e("Exception occurred when determining type of OneWireDevice. Log message throttled until next success.", e);
					continue;
				}
				
				if (type.equals("DS18S20") || type.equals("DS18B20") || type.equals("DS2762")) {
					dev = new OneWireThermometer(0.0);
					dev.setInternalId(s);
					dev.setName("Thermometer " + s);
					session.save(dev);
				} else if (type.equals("DS2423")) {
					dev = new OneWireCounter(0.0);
					dev.setInternalId(s);
					dev.setName("Counter " + s);
					session.save(dev);				
				} else if (loggedSet.add(NOCORR_LOG_DIFF + s)) {
					L.e("Found no corresponding device for 1-wire device with internal ID " + s + " and type " + type + ". Log message throttled until application restart.");
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		List<OneWireDevice> devices = session.createCriteria(OneWireDevice.class).list();
		
		// loop through Homeki OneWireDevices; if not found on 1-wire network, set Homeki OneWireDevice as inactive
		for (OneWireDevice d : devices) {
			boolean found = existsInArray(ids, d.getInternalId());
			
			if (!found && d.isActive()) {
				d.setActive(false);
				L.w("OneWireDevice with ID '" + d.getId() + "' and internal ID '" + d.getInternalId() + "' was not found any longer on the 1-wire network. Log message throttled until device is found again. Device inactivated.");
			} else if (found && !d.isActive()) {
				d.setActive(true);
				L.w("Previously inactivated OneWireDevice with ID '" + d.getId() + "' and internal ID '" + d.getInternalId() + "' was found again on 1-wire network. Device reactivated.");
			}
		}
		
		Hibernate.closeSession(session);
	}
	
	private boolean existsInArray(List<String> strings, String value) {
		for (String s : strings) {
			if (s.equals(value))
				return true;
		}
		return false;
	}
}
