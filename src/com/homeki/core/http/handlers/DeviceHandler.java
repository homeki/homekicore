package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.storage.Hibernate;

public class DeviceHandler extends HttpHandler {
	public enum Actions {
		LIST, SET, BAD_ACTION
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "device"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case LIST:
			resolveList();
			break;
		case SET:
			resolveSet();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveSet() {
		int id = getIntParameter("deviceid");
		String post = getPost();
		
		if (id == -1 || post.equals(""))
			return;
		
		JsonDevice jdev = gson.fromJson(post, JsonDevice.class);
		
		Session session = Hibernate.openSession();
		Device dev = (Device)session.get(Device.class, id);
		
		if (jdev.name != null)
			dev.setName(jdev.name);
		
		session.save(dev);
		Hibernate.closeSession(session);
	}
	
	private void resolveList() {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class).list();
		
		sendString(200, gson.toJson(JsonDevice.convertList(list)));
		
		Hibernate.closeSession(session);
	}
}
