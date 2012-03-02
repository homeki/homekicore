package com.homeki.core.http.handlers;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
	protected void handle(HttpRequest request, HttpResponse response, List<NameValuePair> queryString, String method, StringTokenizer path) {
		path.nextToken(); // dismiss "server"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case GET:
			resolveGet(response);
			break;
		case SET:
			resolveSet(request, response);
			break;
		default:
			sendString(response, 404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveGet(HttpResponse response) {
		Session session = Hibernate.openSession();
		
		String name = Setting.getString(session, SERVER_NAME_KEY);
		
		if (name.length() == 0)
			name = DEFAULT_SERVER_NAME;
		
		Hibernate.closeSession(session);
		
		sendString(response, 200, gson.toJson(new JsonServerInfo(name)));
	}
	
	private void resolveSet(HttpRequest request, HttpResponse response) {
		String post = getPost(request, response);
		
		if (post.equals(""))
			return;
		
		JsonServerInfo jinfo = gson.fromJson(post, JsonServerInfo.class);
		
		if (jinfo.name == null || jinfo.name.length() == 0) {
			sendString(response, 405, "Server name cannot be empty.");
			return;
		}
		
		Session session = Hibernate.openSession();
		Setting.putString(session, SERVER_NAME_KEY, jinfo.name);
		Hibernate.closeSession(session);
		
		sendString(response, 200, "Server information updated successfully.");
	}
}
