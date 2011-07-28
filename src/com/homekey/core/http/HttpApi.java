package com.homekey.core.http;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.DimDeviceCommand;
import com.homekey.core.command.GetStatusCommand;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.http.command.GetJsonDevicesCommand;

public class HttpApi {
	private CommandQueue queue;
	
	public HttpApi(CommandQueue queue) {
		this.queue = queue;
	}
	
	public String getDevices() {
		return (new GetJsonDevicesCommand().postAndWaitForResult(queue));
	}
	
	public boolean switchOn(int id) {
		new SwitchDeviceCommand(id, true).postAndWaitForResult(queue);
		return true;
	}
	
	public boolean switchOff(int id) {
		new SwitchDeviceCommand(id, false).postAndWaitForResult(queue);
		return true;
	}
	
	public boolean dim(int id, int level) {
		new DimDeviceCommand(id, level).postAndWaitForResult(queue);
		return true;
	}
	
	public String getData(int id, Date start, Date end) {
		return null;
	}
	
	public String getStatus(int id) {
		Gson builder = new GsonBuilder().setPrettyPrinting().create();
		return builder.toJson(new GetStatusCommand(id).postAndWaitForResult(queue));
	}
	
}
