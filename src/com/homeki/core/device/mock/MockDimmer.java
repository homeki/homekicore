package com.homeki.core.device.mock;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.abilities.Settable;
import com.homeki.core.main.L;

@Entity
public class MockDimmer extends Device implements Settable {
	public static final int MOCKDIMMER_ONOFF_CHANNEL = 0;
	public static final int MOCKDIMMER_LEVEL_CHANNEL = 1;
	
	public MockDimmer() {
		
	}
	
	public MockDimmer(int defaultLevel) {
		addOnOffHistoryPoint(false);
		addLevelHistoryPoint(defaultLevel);
	}

	@Override
	public void set(int channel, int value) {
		L.i("MockDimmer with internal ID '" + getInternalId() + "' changed channel " + channel + " to " + value + ".");
		
		if (channel == MOCKDIMMER_ONOFF_CHANNEL)
			addOnOffHistoryPoint(value == 1);
		else if (channel == MOCKDIMMER_LEVEL_CHANNEL)
			addLevelHistoryPoint(value);
		else
			throw new RuntimeException("Tried to set invalid channel " + channel + " on MockDimmer with internal ID '" + getInternalId() + "'.");
	}
	
	public void addOnOffHistoryPoint(boolean on) {
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setChannel(MOCKDIMMER_ONOFF_CHANNEL);
		dhp.setValue(on ? 1 : 0);
		historyPoints.add(dhp);
	}
	
	public void addLevelHistoryPoint(int level) {
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setChannel(MOCKDIMMER_LEVEL_CHANNEL);
		dhp.setValue(level);
		historyPoints.add(dhp);
	}

	@Override
	public String getType() {
		return "dimmer";
	}
}
