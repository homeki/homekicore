package com.homekey.core.http;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.http.json.JsonDevice;
import com.homekey.core.http.json.JsonPair;
import com.homekey.core.http.json.JsonStatus;
import com.homekey.core.main.Monitor;
import com.homekey.core.storage.DatumPoint;

public class HttpApi {
	private Gson gson;
	private Monitor monitor;

	public HttpApi(Monitor monitor) {
		this.gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
		this.monitor = monitor;
	}

	public String getDevices() {
		return gson.toJson(JsonDevice.convertList(monitor.getDevices()));
	}

	public void switchOn(int id) {
		Device d = monitor.getDevice(id);
		Switchable s = (Switchable) d;
		s.on();
	}

	public void switchOff(int id) {
		Device d = monitor.getDevice(id);
		Switchable s = (Switchable) d;
		s.off();
	}

	public void dim(int id, int level) {
		Device dev = monitor.getDevice(id);
		Dimmable d = (Dimmable) dev;
		d.dim(level);
	}

	public String getHistory(int id, Date from, Date to) {
		Device dev = monitor.getDevice(id);
		Queryable<?> q = (Queryable<?>)dev;
		List<DatumPoint> points = q.getHistory(from, to);
		return gson.toJson(JsonPair.convertList(points));
	}

	public String getStatus(int id) {
		Device d = monitor.getDevice(id);
		Queryable<?> q = (Queryable<?>) d;
		JsonStatus status = new JsonStatus(q.getValue());
		return gson.toJson(status);
	}
}
