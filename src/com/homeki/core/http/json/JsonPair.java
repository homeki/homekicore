package com.homeki.core.http.json;

import java.util.Date;
import java.util.List;

import com.homeki.core.storage.HistoryPoint;

public class JsonPair {	
	Date registered;
	Object value;
	
	public JsonPair() {
		
	}
	
	public static JsonPair[] convertList(List<HistoryPoint> points) {
		JsonPair[] pairs = new JsonPair[points.size()];
		
		for (int i = 0; i < pairs.length; i++) {
			JsonPair jp = new JsonPair();
			jp.registered = points.get(i).getRegistered();
			jp.value = points.get(i).getValue();
			pairs[i] = jp;
		}
		
		return pairs;
	}
}
