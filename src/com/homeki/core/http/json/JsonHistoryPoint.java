package com.homeki.core.http.json;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.HistoryPoint;

public class JsonHistoryPoint {	
	public Date registered;
	public Integer channel;
	public Object value;
	
	public JsonHistoryPoint() {
		
	}
	
	public static JsonHistoryPoint[] convertList(List<HistoryPoint> points) {
		JsonHistoryPoint[] pairs = new JsonHistoryPoint[points.size()];
		
		for (int i = 0; i < pairs.length; i++) {
			JsonHistoryPoint jp = new JsonHistoryPoint();
			jp.registered = points.get(i).getRegistered();
			jp.channel = points.get(i).getChannel();
			jp.value = points.get(i).getValue();
			pairs[i] = jp;
		}
		
		return pairs;
	}
}
