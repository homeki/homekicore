package com.homeki.core.device.tellstick;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.device.abilities.Switchable;
import com.homeki.core.storage.DatumPoint;

public class TellStickDimmer extends Device implements Dimmable, Switchable, Queryable<Integer> {
	public TellStickDimmer(String internalId) {
		super(internalId);
	}

	@Override
	public void dim(int level) {
		TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		//historyTable.putValue(new Date(), level);
	}

	@Override
	public void off() {
		dim(0);
	}

	@Override
	public Integer getValue() {
		return 0;
		//return (Integer)historyTable.getLatestValue();
	}
	
	@Override
	public void on() {
		dim(255);
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		//return historyTable.getValues(from, to);
		return null;
	}

	@Override
	protected Type getTableValueType() {
		return Integer.class;
	}

	@Override
	public String getType() {
		return "dimmer";
	}
}
