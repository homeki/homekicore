package com.homeki.core.http;

import java.util.StringTokenizer;

public class HttpTriggerHandler extends HttpHandler {
	public enum Actions {
		GET, LINK, UNLINK, DELETE, BAD_ACTION
	}
	
	public HttpTriggerHandler(HttpApi api) {
		super(api);
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "trigger"
		
		Actions action = Actions.BAD_ACTION;
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		//case GET:
		//	resolveGet();
		//	break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	//private void resolveGet() {
	//	
	//}
}
