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

		if (items == null) {
			if (loggedSet.add(DETECTOR))
				L.e("1-wire network not found. Detection of devices failed. Log message throttled until next success.");
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
		
		// loop through 1-wire network; if device in homeki db is not found, create it
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
					L.e("Found no corresponding device for 1-wire device with internal id " + s + " and type " + type + ". Log message throttled until application restart.");
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		List<OneWireDevice> devices = session.createCriteria(OneWireDevice.class).list();
		
		// loop through homeki onewire devices; if not found on 1-wire network, set homeki onewire device as inactive
		for (OneWireDevice d : devices) {
			boolean found = existsInArray(ids, d.getInternalId());
			
			if (!found && d.getActive()) {
				d.setActive(false);
				L.w("OneWireDevice with ID '" + d.getId() + "' and internal ID '" + d.getInternalId() + "' was not found any longer on the 1-wire network. Log message throttled until device is found again. Device inactivated.");
			} else if (found && !d.getActive()) {
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
