package com.homeki.core.device.mock;

import java.util.Date;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.device.DimmerHistoryPoint;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.main.L;

@Entity
public class MockDimmer extends Device implements Switchable, Dimmable {
	public MockDimmer() {

	}
	
	@Override
	public void dim(int level) {
		L.i("MockHistoryDimmerDevice '" + getInternalId() + "' now has dim level " + level + ".");
		
		DimmerHistoryPoint dhp = new DimmerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(level);
		historyPoints.add(dhp);
	}
	
	@Override
	public void off() {
		dim(0);
	}
	
	@Override
	public void on() {
		dim(255);
	}
	
	@Override
	public String getType() {
		return "dimmer";
	}
}
