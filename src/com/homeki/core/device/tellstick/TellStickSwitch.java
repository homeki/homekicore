package com.homeki.core.device.tellstick;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.Queryable;
import com.homeki.core.device.Switchable;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.ITableFactory;
import com.homeki.core.threads.TellstickCommandDispatcherThread;

public class TellStickSwitch extends Device implements Switchable, Queryable<Boolean> {
	
	
	public TellStickSwitch(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}
	
	@Override
	public void off() {
		TellstickCommandDispatcherThread.off(getInternalId());
		historyTable.putValue(new Date(), false);
	}
	
	@Override
	public void on() {
		TellstickCommandDispatcherThread.on(getInternalId());
		historyTable.putValue(new Date(), true);
	}
	
	@Override
	public Boolean getValue() {
		return (Boolean)historyTable.getLatestValue();
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return historyTable.getValues(from, to);
	}

	@Override
	protected Type getTableValueType() {
		return Boolean.class;
	}
}
