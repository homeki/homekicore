package com.homekey.core.http.json;

import com.google.gson.JsonElement;


public class JsonStatus {
	@SuppressWarnings("unused")
	private Object status;
	
	public JsonStatus(Object status) {
		this.status = status;
	}

	public static JsonStatus wrongId() {
		return new JsonStatus("incorrect id");
	}
}
