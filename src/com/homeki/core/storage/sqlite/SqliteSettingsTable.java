package com.homeki.core.storage.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.homeki.core.log.L;
import com.homeki.core.storage.ISettingsTable;

public class SqliteSettingsTable extends SqliteTable implements ISettingsTable {
	protected SqliteSettingsTable(String databaseName) {
		super(databaseName);
	}
	
	@Override
	public void ensureTable() {
		if (!tableExists("settings")) {
			String sql = "CREATE TABLE devices(id INTEGER PRIMARY KEY AUTOINCREMENT," +
											  "key STRING, " +
											  "value STRING)";
			executeUpdate(sql);
		}
	}

	public void executeSql(String sql, String key, String value) {
		Connection conn = openConnection();
		
		try {
			PreparedStatement stat = conn.prepareStatement("INSERT INTO settings(key, value) VALUES(?, ?)");
			stat.setString(1, key);
			stat.setString(2, value);
			stat.executeUpdate();
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't insert value in database.", e);
		}
		finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void setString(String key, String value) {
		setValue(key, value);
	}

	@Override
	public String getString(String key) {
		return getValue(key);
	}

	@Override
	public void setInt(String key, int value) {
		setValue(key, Integer.toString(value));
	}

	@Override
	public int getInt(String key) {
		return 0;
	}
	
	private String getValue(String key) {
		int count = getScalar("SELECT COUNT(id) FROM settings WHERE key = ?", key);
		
		if (count == 0)
			return "";
		
		return (String)getField("SELECT value FROM settings WHERE key = ?", key, String.class);
	}
	
	private void setValue(String key, String value) {
		int count = getScalar("SELECT COUNT(id) FROM settings WHERE key = ?", key);
		
		if (count == 0)
			executeSql("INSERT INTO settings(key, value) VALUES(?, ?)", key, value);
		else
			executeSql("UPDATE settings SET value = ? WHERE key = ?", key, value);
	}
}