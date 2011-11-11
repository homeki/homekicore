package com.homeki.core.http;

import java.util.StringTokenizer;

import com.homeki.core.log.L;

public class HttpSetHandler extends HttpHandler {
	public enum Actions {
		ON, OFF, DIM, DEVICE, BAD_ACTION
	}
	
	public HttpSetHandler(HttpApi api) {
		super(api);
	}
	
	@Override
	protected void handle(String method, StringTokenizer path) {
		Actions action = Actions.BAD_ACTION;
		
		try {
			action = Actions.valueOf(path.nextToken().toUpperCase());
		} catch (Exception ex) {}
		
		try {
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
		} catch (Exception ex) {
			L.e("Unknown exception occured while processing HTTP request.", ex);
			try {
				sendString(405, "Something went wrong while processing the HTTP request.");
			} catch (Exception ignore) {}
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
		
		try {
			api.setDevice(id, post);
		}
		catch (Exception ex) {
			sendString(405, "Problem parsing POST JSON for set device.");
		}
		
		// TODO: lägg till så att det loggas fel i L vid alla sendString
		// TODO: kolla så att exception kastas från HttpApi:et, och se till så att alla fångas och skrivs ut här
		// TODO: fixa setDevice så att den kollar vilka fält som är satta och sedan kör update på endast de som är satta
		// TODO: fixa samma på Android-klienten, så att asynctasken har setters för det man kan sätta på en device och så
		//       att den använder ett JsonDevice som serialiseras till JSON
	}
}
