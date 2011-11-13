package com.homeki.core.http;

import java.io.File;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.http.entity.FileEntity;

import com.homeki.core.log.L;

public class HttpGetHandler extends HttpHandler {
	public enum Actions {
		TIME, DEVICES, STATUS, HISTORY, IMAGE, BAD_ACTION
	}
	
	public HttpGetHandler(HttpApi api) {
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
			case DEVICES:
				resolveDevices();
				break;
			case TIME:
				resolveTime();
				break;
			case STATUS:
				resolveStatus();
				break;
			case IMAGE:
				resolveImage();
				break;
			case HISTORY:
				resolveHistory();
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
	
	private void resolveImage() {
		L.i("woho!");
		int id = getIntParameter("id");
		String path = api.getSnapshotPath(id);
		response.setEntity(new FileEntity(new File(path), "image"));
	}
	
	private void resolveDevices() {
		sendString(200, api.getDevices());
	}
	
	private void resolveTime() {
		sendString(200, new Date().toString());
	}
	
	private void resolveStatus() {
		int id = getIntParameter("id");
		
		if (id == -1)
			return;
		
		try {
			sendString(200, api.getStatus(id));
		} catch (ClassCastException ex) {
			sendString(405, "Device with id " + id + " is not queryable for status.");
		}
	}
	
	private void resolveHistory() {
		int id = getIntParameter("id");
		Date from = getDateParameter("from");
		Date to = getDateParameter("to");
		
		if (id == -1 || from == null || to == null)
			return;
		
		try {
			sendString(200, api.getHistory(id, from, to));
		} catch (ClassCastException ex) {
			sendString(405, "Device with id " + id + " is not queryable for history.");
		}
	}
}
