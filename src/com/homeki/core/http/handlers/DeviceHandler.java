package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
	protected void handle(HttpRequest request, HttpResponse response, List<NameValuePair> queryString, String method, StringTokenizer path) {
		path.nextToken(); // dismiss "device"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case LIST:
			resolveList(response);
			break;
		case SET:
			resolveSet(request, response, queryString);
			break;
		default:
			sendString(response, 404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveSet(HttpRequest request, HttpResponse response, List<NameValuePair> queryString) {
		int id = getIntParameter(response, queryString, "deviceid");
		String post = getPost(request, response);
		
		if (id == -1 || post.equals(""))
			return;
		
		JsonDevice jdev = gson.fromJson(post, JsonDevice.class);
		
		Session session = Hibernate.openSession();
		Device dev = (Device)session.get(Device.class, id);
		
		if (jdev.name != null)
			dev.setName(jdev.name);
		if (jdev.description != null)
			dev.setDescription(jdev.description);
		
		session.save(dev);
		Hibernate.closeSession(session);
		
		sendString(response, 200, "Device updated successfully.");
	}
	
	private void resolveList(HttpResponse response) {
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class).list();
		
		sendString(response, 200, gson.toJson(JsonDevice.convertList(list, session)));
		
		Hibernate.closeSession(session);
	}
}
