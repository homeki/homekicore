package com.homekey.core.http;

import java.util.Date;

import com.homekey.core.http.json.JsonData;
import com.homekey.core.http.json.JsonDevice;
import com.homekey.core.http.json.JsonStatus;
import com.homekey.core.main.Monitor;

public class HttpApi {
	
	private Monitor monitor;

	public HttpApi(Monitor monitor){
		this.monitor = monitor;
	}
	
	JsonDevice[] getDevices() {
		return null;
	}
	
	void switchOn(int id) {
		
	}
	
	void switchOff(int id) {
		
	}
	
	void dim(int id, int level) {
		
	}
	
	JsonStatus getStatus() {
		return null;
	}
	
	JsonData getData(int id, Date start, Date end) {
		return null;
	}
}
