package com.homeki.core.http.handlers;

import java.util.StringTokenizer;

import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonServerInfo;

public class InfoHandler extends HttpHandler {
	public enum Actions {
		INFO, BAD_ACTION
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "server"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case INFO:
			resolveInfo();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveInfo() {
		sendString(200, gson.toJson(new JsonServerInfo()));
	}
}
