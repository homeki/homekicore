package com.homekey.core.http;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.GetStatusCommand;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.device.Device;
import com.homekey.core.http.json.JsonData;
import com.homekey.core.http.json.JsonDevice;
import com.homekey.core.http.json.JsonStatus;
import com.homekey.core.main.InternalData;

public class HttpApi {
	private CommandQueue queue;

	public HttpApi(CommandQueue queue){
		this.queue = queue;
	}
	
	public String getDevices() {
//		JsonObject jo = new JsonObject();
//		Gson g = new GsonBuilder().setPrettyPrinting().create();
//		for (Device d : devices.values())
//			jo.add(d.getClass().getSimpleName(), g.toJsonTree(d));
//		return g.toJson(jo);
		return "";
	}
	
	public boolean switchOn(int id) {
		//new SwitchDeviceCommand(id, true).postAndWaitForResult(this);
		return false;
	}
	
	public boolean switchOff(int id) {
		//new SwitchDeviceCommand(id, false).postAndWaitForResult(this);
		return false;
	}
	
	public boolean dim(int id, int level) {
		return false;
	}
	
	public String getStatus(int id) {
		//T status =  new GetStatusCommand<T>(id).postAndWaitForResult(this);
		return null;
	}
	
	public String getData(int id, Date start, Date end) {
		return null;
	}
}
