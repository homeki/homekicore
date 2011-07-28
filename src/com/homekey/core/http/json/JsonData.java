package com.homekey.core.http.json;

import java.util.Date;

public class JsonData<T> {
	T data;
	Date registered;
	
	public JsonData(T data, Date registered) {
		this.data = data;
		this.registered = registered;
	}
	
	public JsonData(T data){
		this.data = data;
		this.registered = new Date();
	}
}
