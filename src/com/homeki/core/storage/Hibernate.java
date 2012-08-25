package com.homeki.core.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.context.ManagedSessionContext;
import org.hibernate.proxy.HibernateProxy;

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
    
    @SuppressWarnings("unchecked")
	public static <T> T unproxy(T entity) {
        if (entity == null)
            throw new NullPointerException("Entity passed for unproxy is null.");

        org.hibernate.Hibernate.initialize(entity);
        
        if (entity instanceof HibernateProxy)
            entity = (T)((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        
        return entity;
    }
}