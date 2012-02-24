package com.homeki.core.device.tellstick;

import java.util.List;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.main.Module;
import com.homeki.core.storage.Hibernate;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	
	public TellStickModule() {

	}
	
	@Override
	public void construct() {
		TellStickNative.open();
		synchronizeWithNativeDevices();
		
		listenerThread = new TellStickListener();
		listenerThread.start();
	}

	@Override
	public void destruct() {
		listenerThread.shutdown();
		TellStickNative.close();
	}
	
	private void synchronizeWithNativeDevices() {
		int[] ids = TellStickNative.getDeviceIds(); // returns NULL if no devices are found
		Session session = Hibernate.openSession();
		
		if (ids != null) {
			// loop through tellsticknative devices; if device in homeki db is not found, create it
			for (int id : ids) {
				String sid = String.valueOf(id);
				String type = TellStickNative.getDeviceType(id);
				
				Device dev = Device.getByInternalId(session, sid);
				
				if (dev == null) {
					if (type.equals("switch"))
						dev = new TellStickSwitch(false);
					else if (type.equals("dimmer"))
						dev = new TellStickDimmer(0);
					dev.setInternalId(sid);
					session.save(dev);
				} else {
					dev.setActive(true);
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		List<TellStickDevice> devices = session.createCriteria(TellStickDevice.class).list();
		
		// loop through homeki tellstick devices; if device in tellsticknative not found, set homeki tellstick device as inactive
		for (TellStickDevice dev : devices) {
			int id = -1;
			
			try {
				id = Integer.valueOf(dev.getInternalId());
				boolean found = existsInArray(ids, id);
				
				if (!found) {
					dev.setActive(false);
					L.w("Found TellStickDevice with ID '" + dev.getId() + "' and internal ID '" + dev.getInternalId() + "' which didn't exist as a native device in Telldus API. Device inactivated.");
				} else if (found && !dev.getActive()) {
					dev.setActive(true);
					L.i("Previously inactivated TellStickDevice with ID '" + dev.getId() + "' and internal ID '" + dev.getInternalId() + "' was once again found as a native device in Telldus API. Device reactivated.");
				}
			} catch (NumberFormatException ex) {
				L.e("Failed to convert internal ID '" + dev.getInternalId() + "' of a TellStickDevice to an integer. This should always be possible to do, as the native Telldus API returns only numeric IDs. Something is amiss.");
			}
		}
		
		Hibernate.closeSession(session);
	}
	
	private boolean existsInArray(int[] array, int value) {
		if (array == null)
			return false;
		
		for (int i : array) {
			if (i == value) 
				return true;
		}
		
		return false;
	}
}
