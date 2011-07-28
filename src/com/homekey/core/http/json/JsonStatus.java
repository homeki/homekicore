package com.homekey.core.http.json;

public class JsonStatus<T> {
	public T status;
	public JsonStatus(T status){
		this.status = status;
	}
}
