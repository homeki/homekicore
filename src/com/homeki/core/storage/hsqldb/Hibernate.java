package com.homeki.core.storage.hsqldb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.ManagedSessionContext;

public class Hibernate {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public Session openSession() {
    	Session session = getSessionFactory().openSession();
    	session.beginTransaction();
    	return session;
    }
    
    public void commitSession(Session session) {
        ManagedSessionContext.unbind(getSessionFactory());
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
}