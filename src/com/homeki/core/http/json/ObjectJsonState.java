package com.homeki.core.http.json;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;

public class ObjectJsonState extends JsonState {
	public Object value;
	
	public ObjectJsonState(Session ses, Device d) {
		HistoryPoint p = (HistoryPoint)ses.createFilter(d.getHistoryPoints(), "order by registered desc")
				.setMaxResults(1)
				.uniqueResult();
		value = p.getValue();
	}
}
