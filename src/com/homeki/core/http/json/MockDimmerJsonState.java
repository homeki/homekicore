package com.homeki.core.http.json;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.logging.L;

public class MockDimmerJsonState extends JsonState {
	public Integer value;
	public Integer level;
	
	public MockDimmerJsonState(MockDimmer d) {
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)d.getLatestHistoryPoint(MockDimmer.MOCKDIMMER_ONOFF_CHANNEL);
		IntegerHistoryPoint level = (IntegerHistoryPoint)d.getLatestHistoryPoint(MockDimmer.MOCKDIMMER_LEVEL_CHANNEL);
		
		if (onoff != null) {
			this.value = onoff.getValue();
		} else {
			L.e("Received null value in MockDimmerJsonState for device with ID " + d.getId() + " and name '" + d.getName() + "'. Should only happen after an upgrade, and if this has not been seen in any log for a while, the if-statements can be removed.");
			this.value = 0;
		}
		
		if (level != null) {
			this.level = level.getValue();
		} else {
			L.e("Received null level in MockDimmerJsonState for device with ID " + d.getId() + " and name '" + d.getName() + "'. Should only happen after an upgrade, and if this has not been seen in any log for a while, the if-statements can be removed.");
			this.level = 0;
		}
	}
}
