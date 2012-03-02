package com.homeki.core.http.handlers;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonPair;
import com.homeki.core.storage.Hibernate;

public class DeviceStateHandler extends HttpHandler {
	public enum Actions {
		SET, LIST, BAD_ACTION
	}
	
	@Override
	protected void handle(HttpRequest request, HttpResponse response, List<NameValuePair> queryString, String method, StringTokenizer path) {
		path.nextToken(); // dismiss "device"
		path.nextToken(); // dismiss "state"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case SET:
			resolveSet(response, queryString);
			break;
		case LIST:
			resolveList(response, queryString);
			break;
		default:
			sendString(response, 404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveSet(HttpResponse response, List<NameValuePair> queryString) {
		int id = getIntParameter(response, queryString, "deviceid");
		int value = getIntParameter(response, queryString, "value");

		if (id == -1 || value == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device) session.get(Device.class, id);
		
		if (dev == null) {
			sendString(response, 405, "Could not load device from device ID.");
			return;
		}
		
		boolean on = value == 1;
		// dim
		
		if (dev instanceof Dimmable) {
			try {
				//TODO: FIX THIS (SHOULD BE PARSE OPTIONAL ETC..)
				int level = getIntParameter(response, queryString, "level");
				if (level != -1) {
					((Dimmable)dev).dim(level, on);
				} else {
					Switchable sw = (Switchable) dev;
					
					if (on) {
						sw.on();
					} else {
						sw.off();
					}
					//hack för att ta bort sendstringen från missad parse tidigare
					sendString(response, 200, "");
				}
			} catch (NumberFormatException e) {
				sendString(response, 405, "Failed to parse '" + value + "' as integer.");
			}
		} else if (dev instanceof Switchable) {
			Switchable sw = (Switchable) dev;
			
			if (on)
				sw.on();
			else
				sw.off();
		} else {
			sendString(response, 405, "Device with specified ID is not a switch.");
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveList(HttpResponse response, List<NameValuePair> queryString) {
		int id = getIntParameter(response, queryString, "deviceid");
		Date from = getDateParameter(response, queryString, "from");
		Date to = getDateParameter(response, queryString, "to");
		
		if (id == -1 || from == null || to == null)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device) session.get(Device.class, id);
		
		if (dev == null) {
			sendString(response, 405, "Could not load device from device ID.");
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<HistoryPoint> l = session.createFilter(dev.getHistoryPoints(), "where registered between ? and ? order by registered desc")
				.setTimestamp(0, from)
				.setTimestamp(1, to)
				.list();
		
		sendString(response, 200, gson.toJson(JsonPair.convertList(l)));
		
		Hibernate.closeSession(session);
	}
}
