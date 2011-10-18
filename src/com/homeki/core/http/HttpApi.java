package com.homeki.core.http;

import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homeki.core.device.Device;
import com.homeki.core.device.Dimmable;
import com.homeki.core.device.Queryable;
import com.homeki.core.device.Switchable;
import com.homeki.core.http.json.JsonDevice;
import com.homeki.core.http.json.JsonPair;
import com.homeki.core.http.json.JsonStatus;
import com.homeki.core.log.L;
import com.homeki.core.main.Monitor;
import com.homeki.core.storage.DatumPoint;

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
