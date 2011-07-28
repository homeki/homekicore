package com.homekey.core.http.json;


public class JsonStatus <T>{
	private T status;
	
	public JsonStatus(T status) {
		this.status = status;
	}
}
