package com.homeki.core.storage;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.ManagedSessionContext;

import com.homeki.core.storage.entities.HDevice;
import com.homeki.core.storage.entities.HDimmerHistoryPoint;
import com.homeki.core.storage.entities.HSwitchHistoryPoint;
import com.homeki.core.storage.entities.HTemperatureHistoryPoint;

public class Hibernate {
    private static SessionFactory sessionFactory = null;
    
    private Hibernate() {
    	
    }
    
    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void init() {
    	sessionFactory = new Configuration().configure().buildSessionFactory();
	}
    
    public static Session openSession() {
    	Session session = getSessionFactory().openSession();
    	session.beginTransaction();
    	return session;
    }
    
    public static void closeSession(Session session) {
        ManagedSessionContext.unbind(getSessionFactory());
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
    
	public static List<HistoryPoint> getTemperatureHistoryPoints(Date from, Date to) {
		Session session = openSession();
		@SuppressWarnings("unchecked")
		List<HistoryPoint> list = session.createQuery("from HTemperatureHistoryPoint as p where p.registered between ? and ? order by p.registered asc")
				.setDate(0, from)
				.setDate(1, to)
				.list();
		closeSession(session);
		return list;
	}
	
	public static List<HistoryPoint> getSwitchHistoryPoints(Date from, Date to) {
		Session session = openSession();
		@SuppressWarnings("unchecked")
		List<HistoryPoint> list = session.createQuery("from HSwitchHistoryPoint as p where p.registered between ? and ? order by p.registered asc")
				.setDate(0, from)
				.setDate(1, to)
				.list();
		closeSession(session);
		return list;
	}
	
	public static List<HistoryPoint> getDimmerHistoryPoints(Date from, Date to) {
		Session session = openSession();
		@SuppressWarnings("unchecked")
		List<HistoryPoint> list = session.createQuery("from HDimmerHistoryPoint as p where p.registered between ? and ? order by p.registered asc")
				.setDate(0, from)
				.setDate(1, to)
				.list();
		closeSession(session);
		return list;
	}
	
	public static double getLatestTemperatureHistoryPointValue(Integer id) {
		Session session = Hibernate.openSession();
		Double value = (Double)session.createQuery("select value from HTemperatureHistoryPoint as his where his.device = ? order by his.registered desc")
				.setInteger(0, id)
				.setMaxResults(1)
				.uniqueResult();
		Hibernate.closeSession(session);
		
		if (value == null)
			value = 0.0;
		
		return value;
	}	
	public static Boolean getLatestSwitchHistoryPointValue(Integer id) {
		Session session = Hibernate.openSession();
		Boolean value = (Boolean)session.createQuery("select value from HSwitchHistoryPoint as his where his.device = ? order by his.registered desc")
				.setInteger(0, id)
				.setMaxResults(1)
				.uniqueResult();
		Hibernate.closeSession(session);
		
		if (value == null)
			value = false;
		
		return value;
	}
	
	public static Integer getLatestDimmerHistoryPointValue(Integer id) {
		Session session = Hibernate.openSession();
		Integer value = (Integer)session.createQuery("select value from HDimmerHistoryPoint as his where his.device = ? order by his.registered desc")
				.setInteger(0, id)
				.setMaxResults(1)
				.uniqueResult();
		Hibernate.closeSession(session);
		
		if (value == null)
			value = 0;
		
		return value;
	}
	
	public static void putHistoryValue(Integer id, Object point) {
		Session session = Hibernate.openSession();
		HDevice dev = (HDevice)session.load(HDevice.class, id);
		
		if (point instanceof HSwitchHistoryPoint)
			((HSwitchHistoryPoint)point).setDevice(dev);
		else if (point instanceof HDimmerHistoryPoint)
			((HDimmerHistoryPoint)point).setDevice(dev);
		else if (point instanceof HTemperatureHistoryPoint)
			((HTemperatureHistoryPoint)point).setDevice(dev);
		
		session.save(point);
		Hibernate.closeSession(session);
	}
}