package com.homeki.core.http.json;

import org.hibernate.Session;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.mock.MockDimmer;

public class MockDimmerJsonState extends JsonState {
	public Integer value;
	public Integer level;
	
	public MockDimmerJsonState(Session ses, MockDimmer d) {
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)ses.createFilter(d.getHistoryPoints(), "where channel = ? order by registered desc")
				.setInteger(0, MockDimmer.MOCKDIMMER_ONOFF_CHANNEL)
				.setMaxResults(1)
				.uniqueResult();
		
		IntegerHistoryPoint level = (IntegerHistoryPoint)ses.createFilter(d.getHistoryPoints(), "where channel = ? order by registered desc")
				.setInteger(0, MockDimmer.MOCKDIMMER_LEVEL_CHANNEL)
				.setMaxResults(1)
				.uniqueResult();
		
		this.value = onoff.getValue();
		this.level = level.getValue();
	}
}
