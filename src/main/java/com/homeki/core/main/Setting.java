package com.homeki.core.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
public class Setting {
	public static final String SERVER_NAME = "SERVER_NAME";
	public static final String LOCATION_LONGITUDE = "LOCATION_LONGITUDE";
	public static final String LOCATION_LATITUDE = "LOCATION_LATITUDE";
	public static final String SMTP_HOST = "SMTP_HOST";
	public static final String SMTP_PORT = "SMTP_PORT";
	public static final String SMTP_AUTH = "SMTP_AUTH";
	public static final String SMTP_TLS = "SMTP_TLS";
	public static final String SMTP_USER = "SMTP_USER";
	public static final String SMTP_PASSWORD = "SMTP_PASSWORD";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="setting_key")
	private String key;
	
	@Column(name="setting_value")
	private String value;
	
	public Setting() {
		this.key = "";
		this.value = "";
	}
	
	private static Setting getByKey(Session session, String key) {
		return (Setting) session.createCriteria(Setting.class).add(Restrictions.eq("key", key)).uniqueResult();
	}
	
	public static void putInt(Session session, String key, int value) {
		putString(session, key, String.valueOf(value));
	}
	
	public static void putDouble(Session session, String key, double value) {
		putString(session, key, String.valueOf(value));
	}
	
	public static void putBoolean(Session session, String key, boolean value) {
		putString(session, key, String.valueOf(value));
	}
	
	public static int getInt(Session session, String key) {
		int tmp;
		
		try {
			tmp = Integer.valueOf(getString(session, key));
		} catch (NumberFormatException e) {
			tmp = -1;
		}
		
		return tmp;
	}
	
	public static double getDouble(Session session, String key) {
		double tmp;
		
		try {
			tmp = Double.valueOf(getString(session, key));
		} catch (NumberFormatException e) {
			tmp = -1;
		}
		
		return tmp;
	}
	
	public static boolean getBoolean(Session session, String key) {
		boolean tmp;
		
		try {
			tmp = Boolean.valueOf(getString(session, key));
		} catch (NumberFormatException e) {
			tmp = false;
		}
		
		return tmp;
	}
	
	public static void putString(Session session, String key, String value) {
		Setting setting = getByKey(session, key);
		
		if (setting == null) {
			setting = new Setting();
			setting.key = key;
			session.save(setting);
		}
		
		setting.value = value;
	}
	
	public static String getString(Session session, String key) {
		Setting setting = getByKey(session, key);
		
		if (setting == null)
			return "";
		
		return setting.value;
	}
}
