package com.homeki.core.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Entity
public class Setting {
	@SuppressWarnings("unused")
	@Id
	@GeneratedValue
	private int id;
	
	@SuppressWarnings("unused")
	@Column(name="setting_key")
	private String key;
	
	@Column(name="setting_value")
	private String value;
	
	public Setting() {
		this.key = "";
		this.value = "";
	}
	
	private void setValueAsInt(int value) {
		this.value = String.valueOf(value);
	}
	
	private int getValueAsInt() {
		int tmp;
		
		try {
			tmp = Integer.valueOf(value);
		} catch (NumberFormatException ex) {
			tmp = -1;
		}
		
		return tmp;
	}
	
	private static Setting getByKey(Session session, String key) {
		return (Setting) session.createCriteria(Setting.class).add(Restrictions.eq("key", key)).uniqueResult();
	}
	
	public static void putInt(Session session, String key, int value) {
		Setting setting = getByKey(session, key);
		
		if (setting == null) {
			setting = new Setting();
			setting.key = key;
			session.save(setting);
		}
		
		setting.setValueAsInt(value);
	}
	
	public static int getInt(Session session, String key) {
		Setting setting = getByKey(session, key);
		
		if (setting == null)
			return -1;
		
		return setting.getValueAsInt();
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
