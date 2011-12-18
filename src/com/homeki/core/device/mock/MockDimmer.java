package com.homeki.core.device.mock;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.Logs;
import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Queryable;
import com.homeki.core.log.L;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.HDevice;
import com.homeki.core.storage.entities.HDimmerHistory;

public class MockDimmer extends MockDevice implements Dimmable, Queryable<Integer> {
	public MockDimmer(String internalId) {
		super(internalId);
		L.getLogger(Logs.CORE_MOCK).log("Created MockHistoryDimmerDevice.");
	}

	@Override
	public void dim(int level) {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		HDimmerHistory value = new HDimmerHistory();
		value.setRegistered(new Date());
		value.setDevice(dev);
		value.setValue(level);
		session.save(value);
		Hibernate.closeSession(session);
		L.getLogger(Logs.CORE_MOCK).log("MockHistoryDimmerDevice '" + getInternalId() + "' now has dim level " + level + ".");
	}
	
	@Override
	public void off() {
		dim(0);
		L.getLogger(Logs.CORE_MOCK).log("MockHistoryDimmerDevice '" + getInternalId() + "' is now OFF!");
	}
	
	@Override
	public void on() {
		dim(255);
		L.getLogger(Logs.CORE_MOCK).log("MockHistoryDimmerDevice '" + getInternalId() + "' is now ON!");
	}
	
	@Override
	public Integer getValue() {
		Session session = Hibernate.openSession();
		Integer value = (Integer)session.createQuery("select value from HDimmerHistory as his where his.device = ? order by his.registered desc")
				.setInteger(0, id)
				.setMaxResults(1)
				.uniqueResult();
		Hibernate.closeSession(session);
		
		if (value == null)
			value = 0;
		
		return value;
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
