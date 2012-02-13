package com.homeki.core.http.handlers;

import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickNative;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.main.L;
import com.homeki.core.main.Setting;
import com.homeki.core.storage.Hibernate;

public class DeviceTellstickHandler extends HttpHandler {
	private static String NEXT_HOUSE = "TELLSTICK_NEXT_HOUSE_VALUE";
	private static int SEED = 3764;
	
	public enum Actions {
		ADD, LEARN, BAD_ACTION
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "device"
		path.nextToken(); // dismiss "tellstick"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case ADD:
			resolveAdd();
			break;
		case LEARN:
			resolveLearn();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveAdd() {
		String type = getStringParameter("type");
		
		Session session = Hibernate.openSession();
		
		int nextHouse = Setting.getInt(session, NEXT_HOUSE);
		
		if (nextHouse == -1)
			nextHouse = SEED;
		
		Device dev;
		int result;
		
		if (type.equals("switch")) {
			dev = new TellStickSwitch(false);
			result = TellStickNative.addSwitch(nextHouse, 3);
		} else if (type.equals("dimmer")) {
			dev = new TellStickDimmer(0);
			result = TellStickNative.addDimmer(nextHouse, 3);
		} else {
			sendString(405, "Did not recognize type '" + type + "' as a valid TellStick type.");
			return;
		}
		
		if (result < 0) {
			sendString(405, "Failed to add new TellStick device, error returned from underlying TellStick API.");
			L.e("Failed to add new TellStick device of type '" + type + "', error returned from Homeki JNI library was " + result + ".");
			return;
		}
		
		dev.setInternalId(String.valueOf(result));
		
		Setting.putInt(session, NEXT_HOUSE, nextHouse+1);
		session.save(dev);
		
		Hibernate.closeSession(session);
		
		sendString(200, "TellStick device added successfully.");
	}
	
	private void resolveLearn() {
		int id = getIntParameter("deviceid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, id);
		
		if (dev == null) {
			sendString(405, "No device with specified ID found.");
			return;
		}
		
		if (dev instanceof TellStickSwitch || dev instanceof TellStickDimmer) {
			TellStickNative.learn(Integer.valueOf(dev.getInternalId()));
			sendString(200, "Learn command sent successfully.");
		} else {
			sendString(405, "Device with specified ID does not support the learn command.");
		}
		
		Hibernate.closeSession(session);
	}
}
