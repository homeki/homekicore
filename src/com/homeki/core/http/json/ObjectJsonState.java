package com.homeki.core.http.json;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.main.L;

public class ObjectJsonState extends JsonState {
	public Object value;
	
	public ObjectJsonState(Device d) {
		HistoryPoint p = d.getLatestHistoryPoint(0);
		
		if (p != null) {
			value = p.getValue();
		} else {
			value = 0;
			L.e("Received null value in ObjectJsonState for device with ID " + d.getId() + " and name '" + d.getName() + "'. Especially dangerous in ObjectJsonState as we're not sure about a correct default value here. Should only happen after an upgrade, and if this has not been seen in any log for a while, the if-statements can be removed.");
		}
	}
}
