package com.homeki.core.http.handlers;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.http.HttpApi;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonPair;
import com.homeki.core.http.json.JsonState;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.HistoryPoint;

public class HttpStateHandler extends HttpHandler {
	public enum Actions {
		GET, SET, LIST, BAD_ACTION
	}
	
	public HttpStateHandler(HttpApi api) {
		super(api);
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "status"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case GET:
			resolveGet();
			break;
		case SET:
			resolveSet();
			break;
		case LIST:
			resolveList();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveGet() {
		int id = getIntParameter("id");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, id);
		
		if (dev == null) {
			sendString(405, "Could not load device from device ID.");
			return;
		}
		
		HistoryPoint p = (HistoryPoint)session.createFilter(dev.getHistoryPoints(), "order by registered desc")
				.setMaxResults(1)
				.uniqueResult();
		
		if (p == null) {
			sendString(200, "");
			return;
		}
		
		JsonState status = new JsonState(p.getValue());
		sendString(200, gson.toJson(status));
		
		Hibernate.closeSession(session);
	}
	
	private void resolveSet() {
		int id = getIntParameter("id");
		String value = getStringParameter("value").toLowerCase();
		
		if (id == -1 || value.equals(""))
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, id);
		
		if (dev == null) {
			sendString(405, "Could not load device from device ID.");
			return;
		}
		
		if (value.equals("on") || value.equals("off")) {
			// turn on/turn off
			boolean on = value.equals("on");

			if (dev instanceof Switchable) {
				Switchable sw = (Switchable)dev;
				
				if (on) 
					sw.on();
				else
					sw.off();
			} else {
				sendString(405, "Device with specified ID is not a switch.");
			}
		} else {
			// dim
			Integer level;
			
			if (dev instanceof Dimmable) {
				try {
					level = Integer.parseInt(value);
					((Dimmable)dev).dim(level);
				} catch (NumberFormatException ex) {
					sendString(405, "Failed to parse '" + value + "' as integer.");
				}
			} else {
				sendString(405, "Device with specified ID is not a dimmer.");
			}
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveList() {
		int id = getIntParameter("id");
		Date from = getDateParameter("from");
		Date to = getDateParameter("to");
		
		if (id == -1 || from == null || to == null)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device)session.get(Device.class, id);
		
		if (dev == null) {
			sendString(405, "Could not load device from device ID.");
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<HistoryPoint> l = session.createFilter(dev.getHistoryPoints(), "where registered between ? and ? order by registered desc")
				.setDate(0, from)
				.setDate(1, to)
				.list();
		
		sendString(200, gson.toJson(JsonPair.convertList(l)));
		
		Hibernate.closeSession(session);
	}
}
