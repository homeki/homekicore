package com.homeki.core.http.json;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.mock.MockDimmer;

public class MockDimmerJsonState extends JsonState {
	public Integer value;
	public Integer level;
	
	public MockDimmerJsonState(MockDimmer d) {
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)d.getLatestHistoryPoint(MockDimmer.MOCKDIMMER_ONOFF_CHANNEL);
		IntegerHistoryPoint level = (IntegerHistoryPoint)d.getLatestHistoryPoint(MockDimmer.MOCKDIMMER_LEVEL_CHANNEL);
		this.value = onoff.getValue();
		this.level = level.getValue();
	}
}
