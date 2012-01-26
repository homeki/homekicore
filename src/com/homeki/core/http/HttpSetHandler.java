package com.homeki.core.http;

import java.util.StringTokenizer;

public class HttpSetHandler extends HttpHandler {
	public enum Actions {
		ON, OFF, DIM, DEVICE, BAD_ACTION
	}
	
	public HttpSetHandler(HttpApi api) {
		super(api);
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		path.nextToken(); // dismiss "set"
		
		Actions action = Actions.BAD_ACTION;
		
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		switch (action) {
		case ON:
			resolveOnOff(true);
			break;
		case OFF:
			resolveOnOff(false);
			break;
		case DIM:
			resolveDim();
			break;
		case DEVICE:
			resolveDevice();
			break;
		default:
			sendString(404, "No such action, " + action + ".");
			break;
		}
	}
	
	private void resolveOnOff(boolean on) {
		int id = getIntParameter("id");
		
		if (id == -1)
			return;
		
		try {
			if (on)
				api.switchOn(id);
			else
				api.switchOff(id);
			
			response.setStatusCode(200);
		} catch (ClassCastException ex) {
			sendString(405, "Device with id " + id + " is not switchable.");
		}
	}
	
	private void resolveDim() {
		int id = getIntParameter("id");
		int level = getIntParameter("level");
		
		if (id == -1 || level == -1)
			return;
		
		try {
			api.dim(id, level);
			response.setStatusCode(200);
		} catch (ClassCastException ex) {
			sendString(405, "Device with id " + id + " is not dimmable.");
		}
	}
	
	private void resolveDevice() {
		int id = getIntParameter("id");
		String post = getPost();
		
		if (id == -1 || post.equals(""))
			return;
		
		api.setDevice(id, post);
	}
}
