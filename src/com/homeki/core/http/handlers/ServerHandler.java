package com.homeki.core.http.handlers;

import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.HttpHandler;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;

public class ServerHandler extends HttpHandler {
	private static final String SERVER_NAME_KEY = "SERVER_NAME";

	public enum Actions {
		GET, SET, BAD_ACTION
	}
	
	@Override
	protected void handle(Container c) {
		c.path.nextToken(); // dismiss "server"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(c.path.nextToken().toUpperCase());
		} catch (Exception e) {}
		
		switch (action) {
		case GET:
			resolveGet(c);
			break;
		case SET:
			resolveSet(c);
			break;
		default:
			throw new ApiException("No such URL/action: '" + action + "'.");
		}
	}
	
	private void resolveGet(Container c) {
		String name = Setting.getString(c.ses, SERVER_NAME_KEY);
		set200Response(c, gson.toJson(new JsonServerInfo(name)));
	}
	
	private void resolveSet(Container c) {
		String post = getPost(c);
		
		JsonServerInfo jinfo = gson.fromJson(post, JsonServerInfo.class);
		
		if (jinfo.name == null || jinfo.name.length() == 0)
			throw new ApiException("Server name cannot be empty.");
		
		Setting.putString(c.ses, SERVER_NAME_KEY, jinfo.name);
		
		set200Response(c, "Server information updated successfully.");
	}
}
