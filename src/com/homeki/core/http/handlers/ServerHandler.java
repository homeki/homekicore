package com.homeki.core.http.handlers;

import java.util.StringTokenizer;

import org.hibernate.Session;

import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;
import com.homeki.core.storage.Hibernate;

public class ServerHandler extends HttpHandler {
	private static final String SERVER_NAME_KEY = "SERVER_NAME";
	private static final String DEFAULT_SERVER_NAME = "Homeki";
	
	public enum Actions {
		GET, SET, BAD_ACTION
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "server"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case GET:
			resolveGet();
			break;
		case SET:
			resolveSet();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveGet() {
		Session session = Hibernate.openSession();
		
		String name = Setting.getString(session, SERVER_NAME_KEY);
		
		if (name.length() == 0)
			name = DEFAULT_SERVER_NAME;
		
		Hibernate.closeSession(session);
		
		sendString(200, gson.toJson(new JsonServerInfo(name)));
	}
	
	private void resolveSet() {
		String post = getPost();
		
		if (post.equals(""))
			return;
		
		JsonServerInfo jinfo = gson.fromJson(post, JsonServerInfo.class);
		
		if (jinfo.name == null || jinfo.name.length() == 0) {
			sendString(405, "Server name cannot be empty.");
			return;
		}
		
		Session session = Hibernate.openSession();
		Setting.putString(session, SERVER_NAME_KEY, jinfo.name);
		Hibernate.closeSession(session);
		
		sendString(200, "Server information updated successfully.");
	}
}
