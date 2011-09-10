package com.homekey.core.http.json;

import java.util.Date;
import java.util.List;

import com.homekey.core.storage.DatumPoint;

public class JsonPair {	
	Date registered;
	Object value;
	
	public JsonPair() {
		
	}
	
	public static JsonPair[] convertList(List<DatumPoint> points) {
		JsonPair[] pairs = new JsonPair[points.size()];
		
		for (int i = 0; i < pairs.length; i++) {
			JsonPair jp = new JsonPair();
			jp.registered = points.get(i).registered;
			jp.value = points.get(i).value;
			pairs[i] = jp;
		}
		
		return pairs;
	}
}
