package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.Trigger;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonTimerTrigger;
import com.homeki.core.storage.Hibernate;

public class TriggerHandler extends HttpHandler {
	public enum Actions {
		LIST, LINK, UNLINK, DELETE, BAD_ACTION
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "trigger"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case LIST:
			resolveList();
			break;
		case DELETE:
			resolveDelete();
			break;
		case LINK:
			resolveLink();
			break;
		case UNLINK:
			resolveUnlink();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveList() {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Trigger> list = session.createCriteria(Trigger.class).list();
		
		sendString(200, gson.toJson(JsonTimerTrigger.convertList(list)));
		
		Hibernate.closeSession(session);
	}
	
	private void resolveDelete() {
		int id = getIntParameter("triggerid");

		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Trigger trigger = (Trigger)session.get(Trigger.class, id);
		
		if (trigger == null) {
			sendString(405, "No trigger with specified ID.");
		} else { 
			session.delete(trigger);
			sendString(200, "Trigger deleted.");
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveLink() {
		int deviceid = getIntParameter("deviceid");
		int triggerid = getIntParameter("triggerid");
		
		if (deviceid == -1 || triggerid == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, deviceid);
		Trigger tri = (Trigger)session.get(Trigger.class, triggerid);
		
		if (dev == null) {
			sendString(405, "No device with specified ID found.");
		} else if (tri == null) {
			sendString(405, "No trigger with specified ID found.");
		} else if (dev.getTriggers().contains(tri)) {
			sendString(405, "The specified device and the specified trigger are already linked.");
		}
		else {
			dev.getTriggers().add(tri);
			session.save(dev);
			sendString(200, "Specified device and specified trigger successfully linked.");
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveUnlink() {
		int deviceid = getIntParameter("deviceid");
		int triggerid = getIntParameter("triggerid");
		
		if (deviceid == -1 || triggerid == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, deviceid);
		Trigger tri = (Trigger)session.get(Trigger.class, triggerid);
		
		if (dev == null) {
			sendString(405, "No device with specified ID found.");
		} else if (tri == null) {
			sendString(405, "No trigger with specified ID found.");
		} else if (!dev.getTriggers().contains(tri)) {
			sendString(405, "Specified device does not contain the specified trigger.");
		} else {
			dev.getTriggers().remove(tri);
			session.save(dev);
			sendString(200, "Link between specified device and specified trigger successfully removed.");
		}
		
		Hibernate.closeSession(session);
	}
}
