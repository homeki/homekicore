package com.homeki.core.http.handlers;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "device"
		path.nextToken(); // dismiss "state"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
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
	
	private void resolveSet() {
		int id = getIntParameter("deviceid");
		int value = getIntParameter("value");

		if (id == -1 || value == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device) session.get(Device.class, id);
		
		if (dev == null) {
			sendString(405, "Could not load device from device ID.");
			return;
		}
		
		boolean on = value == 1;
		// dim
		
		if (dev instanceof Dimmable) {
			try {
				//TODO: FIX THIS (SHOULD BE PARSE OPTIONAL ETC..)
				int level = getIntParameter("level");
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
					sendString(200, "");
				}
			} catch (NumberFormatException e) {
				sendString(405, "Failed to parse '" + value + "' as integer.");
			}
		} else if (dev instanceof Switchable) {
			Switchable sw = (Switchable) dev;
			
			if (on)
				sw.on();
			else
				sw.off();
		} else {
			sendString(405, "Device with specified ID is not a switch.");
		}
		
		Hibernate.closeSession(session);
	}
	
	private void resolveList() {
		int id = getIntParameter("deviceid");
		Date from = getDateParameter("from");
		Date to = getDateParameter("to");
		
		if (id == -1 || from == null || to == null)
			return;
		
		Session session = Hibernate.openSession();
		
		Device dev = (Device) session.get(Device.class, id);
		
		if (dev == null) {
			sendString(405, "Could not load device from device ID.");
			return;
		}
		
		@SuppressWarnings("unchecked")
		List<HistoryPoint> l = session.createFilter(dev.getHistoryPoints(), "where registered between ? and ? order by registered desc").setDate(0, from).setDate(1, to).list();
		
		sendString(200, gson.toJson(JsonPair.convertList(l)));
		
		Hibernate.closeSession(session);
	}
}
