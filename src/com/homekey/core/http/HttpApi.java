package com.homekey.core.http;

import java.util.Date;

import com.homekey.core.http.json.JsonData;
import com.homekey.core.http.json.JsonDevice;
import com.homekey.core.http.json.JsonStatus;

public class HttpApi {
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
