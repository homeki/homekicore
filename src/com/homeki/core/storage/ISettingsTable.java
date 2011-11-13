package com.homeki.core.storage;

public interface ISettingsTable {
	void ensureTable();
	void setString(String key, String value);
	String getString(String key);
	void setInt(String key, int value);
	int getInt(String key);
}
