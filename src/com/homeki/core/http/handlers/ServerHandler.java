package com.homeki.core.http.handlers;

import java.util.StringTokenizer;

import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonServerInfo;

public class ServerHandler extends HttpHandler {
	public enum Actions {
		GET, BAD_ACTION
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
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveGet() {
		sendString(200, gson.toJson(new JsonServerInfo()));
	}
}
