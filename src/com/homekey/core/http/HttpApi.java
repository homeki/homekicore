package com.homekey.core.http;

import java.util.Date;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Queryable;
import com.homekey.core.device.Switchable;
import com.homekey.core.http.json.JsonDevice;
import com.homekey.core.http.json.JsonStatus;
import com.homekey.core.main.Monitor;

public class HttpApi {

	private Monitor monitor;

	public HttpApi(Monitor monitor) {
		this.monitor = monitor;
	}

	public String getDevices() {
		return new Gson().toJson(JsonDevice.makeArray(monitor.getDevices()));
	}

	public void switchOn(int id) {
		Device d = monitor.getDevice(id);
		if (monitor.hasDevice(id)) {
			Switchable s = (Switchable) d;
			s.on();
		} else {

		}
	}

	public void switchOff(int id) {
		Device d = monitor.getDevice(id);
		if (monitor.hasDevice(id)) {
			Switchable s = (Switchable) d;
			s.off();
		} else {

		}
	}

	public void dim(int id, int level) {
		Device dev = monitor.getDevice(id);
		Dimmable d = (Dimmable) dev;
		d.dim(level);
	}

	public String getData(int id, Date start, Date end) {
		return null;
	}

	public String getStatus(int id) {
		Device d = monitor.getDevice(id);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if (d != null) {
			Queryable<?> q = (Queryable<?>) d;
			JsonStatus status = new JsonStatus(q.getValue());
			return gson.toJson(status);
		}
		throw new NoSuchElementException("There is no device with id = " + id);
	}
}
