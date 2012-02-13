package com.homeki.core.http.json;

import com.homeki.core.device.DimmerHistoryPoint;
import com.homeki.core.device.HistoryPoint;

public class JsonState {
	public Object value;
	public Integer level;
	
	public JsonState(HistoryPoint hp) {
		value = hp.getValue();
		if (hp instanceof DimmerHistoryPoint) {
			level = ((DimmerHistoryPoint)hp).getLevel();
		}
	}
}
