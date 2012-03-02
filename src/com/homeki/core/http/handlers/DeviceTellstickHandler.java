package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.tellstick.TellStickDevice;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickLearnable;
import com.homeki.core.device.tellstick.TellStickSwitch;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.http.json.JsonTellStickDevice;
import com.homeki.core.main.Setting;
import com.homeki.core.storage.Hibernate;

public class DeviceTellstickHandler extends HttpHandler {
	private static final String NEXT_HOUSE_KEY = "TELLSTICK_NEXT_HOUSE_VALUE";
	private static final int HOUSE_SEED = 3764;
	private static final int UNIT = 3;
	
	public enum Actions {
		ADD, LIST, LEARN, BAD_ACTION
	}
	
	@Override
	protected void handle(HttpRequest request, HttpResponse response, List<NameValuePair> queryString, String method, StringTokenizer path) {
		path.nextToken(); // dismiss "device"
		path.nextToken(); // dismiss "tellstick"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case ADD:
			resolveAdd(request, response);
			break;
		case LIST:
			resolveList(response);
			break;
		case LEARN:
			resolveLearn(response, queryString);
			break;
		default:
			sendString(response, 404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveAdd(HttpRequest request, HttpResponse response) {
		String post = getPost(request, response);
		JsonTellStickDevice jsonDevice = gson.fromJson(post, JsonTellStickDevice.class);
		
		Session session = Hibernate.openSession();
		
		int house;
		int unit;
		
		if (jsonDevice.house == null) {
			house = Setting.getInt(session, NEXT_HOUSE_KEY);
			if (house == -1)
				house = HOUSE_SEED;
		} else {
			house = jsonDevice.house;
		}
		if (jsonDevice.unit == null) {
			unit = UNIT;
		} else {
			unit = jsonDevice.unit;
		}
		
		Device dev;

		if (jsonDevice.type.equals("switch")) {
			dev = new TellStickSwitch(false, house, unit);
		} else if (jsonDevice.type.equals("dimmer")) {
			dev = new TellStickDimmer(0, house, unit);
		} else {
			sendString(response, 405, "Did not recognize type '" + jsonDevice.type + "' as a valid TellStick type.");
			return;
		}
		
		if (jsonDevice.house == null)
			Setting.putInt(session, NEXT_HOUSE_KEY, house+1);
		
		session.save(dev);
		
		Hibernate.closeSession(session);
		
		JsonDevice newid = new JsonDevice();
		newid.id = dev.getId();
		sendString(response, 200, gson.toJson(newid));
	}
	
	private void resolveList(HttpResponse response) {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(TellStickDevice.class).list();
		
		sendString(response, 200, gson.toJson(JsonDevice.convertList(list, session)));
		
		Hibernate.closeSession(session);
	}
	
	private void resolveLearn(HttpResponse response, List<NameValuePair> queryString) {
		int id = getIntParameter(response, queryString, "deviceid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		TellStickDevice dev = (TellStickDevice)session.get(TellStickDevice.class, id);
		
		if (dev == null) {
			sendString(response, 405, "No TellStick device with specified ID found.");
			return;
		}
		if (!(dev instanceof TellStickLearnable)) {
			sendString(response, 405, "Specified TellStick device does not support learn.");
			return;
		}
		
		((TellStickLearnable)dev).learn();
		sendString(response, 200, "Learn command sent successfully.");
		
		Hibernate.closeSession(session);
	}
}
