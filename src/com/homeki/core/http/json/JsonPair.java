package com.homeki.core.http.json;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.HistoryPoint;

public class JsonPair {	
	public Date registered;
	public JsonState state;
	
	public JsonPair() {
		
	}
	
	public static JsonPair[] convertList(List<HistoryPoint> points) {
		JsonPair[] pairs = new JsonPair[points.size()];
		
		for (int i = 0; i < pairs.length; i++) {
			JsonPair jp = new JsonPair();
			jp.registered = points.get(i).getRegistered();
			jp.state = new JsonState(points.get(i));
			pairs[i] = jp;
		}
		
		return pairs;
	}
}
