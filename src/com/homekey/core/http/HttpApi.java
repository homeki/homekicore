package com.homekey.core.http;

import java.util.Date;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.GetStatusCommand;

public class HttpApi {
	private CommandQueue queue;

	public HttpApi(CommandQueue queue){
		this.queue = queue;
	}
	
	public String getDevices() {
		return (new GetJsonDevicesCommand().postAndWaitForResult(queue));
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
