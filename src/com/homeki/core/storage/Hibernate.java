package com.homeki.core.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.context.ManagedSessionContext;

public class Hibernate {
    private static SessionFactory sessionFactory = null;
    
    private Hibernate() {
    	
    }
    
    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void init() {
    	sessionFactory = new Configuration().setNamingStrategy(new ImprovedNamingStrategy()).configure().buildSessionFactory();
	}
    
    public static Session currentSession() {
    	return getSessionFactory().getCurrentSession();
    }
    
    public static Session openSession() {
    	Session session = getSessionFactory().openSession();
    	ManagedSessionContext.bind((org.hibernate.classic.Session)session);
    	session.beginTransaction();
    	return session;
    }
    
    public static void closeSession(Session session) {
        ManagedSessionContext.unbind(getSessionFactory());
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
    
    public static void shutdownDatabase() {
    	if (sessionFactory == null)
    		return;
    	
    	Session s = openSession();
    	s.createSQLQuery("SHUTDOWN").executeUpdate();
    	closeSession(s);
    }
}