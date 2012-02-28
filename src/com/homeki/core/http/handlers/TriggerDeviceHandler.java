package com.homeki.core.http.handlers;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.TimerTrigger;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDeviceLink;
import com.homeki.core.storage.Hibernate;

public class TriggerDeviceHandler extends HttpHandler {
	public enum Actions {
		BAD_ACTION, LIST
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "trigger"
		path.nextToken(); // dismiss "timer"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case LIST:
			resolveList();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}

	//TODO: Kan göras snyggare, men det här funkar
	private void resolveList() {
		int id = getIntParameter("triggerid");
		
		if (id == -1)
			return;
		
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class).list();
		
		TimerTrigger trigger = (TimerTrigger) session.get(TimerTrigger.class, id);

		if (trigger == null) {
			sendString(405, "No timer trigger with specified ID.");
			return;
		}
		
		Set<Device> linkedDevices = trigger.getDevices();

		JsonDeviceLink[] jsonDevices = JsonDeviceLink.convertList(list);
		
		for (int i = 0; i < jsonDevices.length; i++)
			jsonDevices[i].setLinked(linkedDevices.contains(list.get(i)));
		
		sendString(200, gson.toJson(jsonDevices));
		
		Hibernate.closeSession(session);
	}
}
