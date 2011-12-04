package com.homeki.core.device.tellstick;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.Dimmable;
import com.homeki.core.device.Queryable;
import com.homeki.core.device.Switchable;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.ITableFactory;
import com.homeki.core.threads.TellStickCommandDispatcherThread;

public class TellStickDimmer extends Device implements Dimmable ,Switchable,Queryable<Integer>{

	public TellStickDimmer(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}

	@Override
	public void dim(int level) {
		//TellStickCommandDispatcherThread.dim(level, getInternalId());
		TellStickNative.dim(Integer.parseInt(getInternalId()), level);
		historyTable.putValue(new Date(), level);
	}

	@Override
	public void off() {
		dim(0);
	}

	@Override
	public Integer getValue() {
		return (Integer)historyTable.getLatestValue();
	}
	
	@Override
	public void on() {
		dim(255);
	}

	@Override
	public List<DatumPoint> getHistory(Date from, Date to) {
		return historyTable.getValues(from, to);
	}

	@Override
	protected Type getTableValueType() {
		return Integer.class;
	}
}
