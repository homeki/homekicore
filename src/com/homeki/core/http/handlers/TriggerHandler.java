package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
	protected void handle(HttpRequest request, HttpResponse response, List<NameValuePair> queryString, String method, StringTokenizer path) {
		path.nextToken(); // dismiss "trigger"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case LIST:
			resolveList(response);
			break;
		case DELETE:
			resolveDelete(response, queryString);
			break;
		case LINK:
			resolveLink(response, queryString);
			break;
		case UNLINK:
			resolveUnlink(response, queryString);
			break;
		default:
			sendString(response, 404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveList(HttpResponse response) {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Trigger> list = session.createCriteria(Trigger.class).list();
		
		sendString(response, 200, gson.toJson(JsonTimerTrigger.convertList(list)));
		
		Hibernate.closeSession(session);
	}
	
	private void resolveDelete(HttpResponse response, List<NameValuePair> queryString) {
		int id = getIntParameter(response, queryString, "triggerid");

		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Trigger trigger = (Trigger)session.get(Trigger.class, id);
		
		if (trigger == null) {
			sendString(response, 405, "No trigger with specified ID.");
		} else { 
			session.delete(trigger);
			sendString(response, 200, "Trigger deleted.");
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveLink(HttpResponse response, List<NameValuePair> queryString) {
		int deviceid = getIntParameter(response, queryString, "deviceid");
		int triggerid = getIntParameter(response, queryString, "triggerid");
		
		if (deviceid == -1 || triggerid == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, deviceid);
		Trigger tri = (Trigger)session.get(Trigger.class, triggerid);
		
		if (dev == null) {
			sendString(response, 405, "No device with specified ID found.");
		} else if (tri == null) {
			sendString(response, 405, "No trigger with specified ID found.");
		} else if (dev.getTriggers().contains(tri)) {
			sendString(response, 405, "The specified device and the specified trigger are already linked.");
		}
		else {
			dev.getTriggers().add(tri);
			session.save(dev);
			sendString(response, 200, "Specified device and specified trigger successfully linked.");
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveUnlink(HttpResponse response, List<NameValuePair> queryString) {
		int deviceid = getIntParameter(response, queryString, "deviceid");
		int triggerid = getIntParameter(response, queryString, "triggerid");
		
		if (deviceid == -1 || triggerid == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, deviceid);
		Trigger tri = (Trigger)session.get(Trigger.class, triggerid);
		
		if (dev == null) {
			sendString(response, 405, "No device with specified ID found.");
		} else if (tri == null) {
			sendString(response, 405, "No trigger with specified ID found.");
		} else if (!dev.getTriggers().contains(tri)) {
			sendString(response, 405, "Specified device does not contain the specified trigger.");
		} else {
			dev.getTriggers().remove(tri);
			session.save(dev);
			sendString(response, 200, "Link between specified device and specified trigger successfully removed.");
		}
		
		Hibernate.closeSession(session);
	}
}
