package com.homekey.core.storage.sqlite;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.homekey.core.log.L;
import com.homekey.core.storage.IHistoryTable;

public class SqliteHistoryTable extends SqliteTable implements IHistoryTable {
	private final String tableName;
	private final Type valueType;
	
	protected SqliteHistoryTable(String databasePath, String tableName, Type valueType) {
		super(databasePath);
		this.tableName = tableName;
		this.valueType = valueType;
	}
	
	@Override
	public void ensureTable() {
		if (!tableExists(tableName)) {
			String columnType;
			
			if (valueType == Boolean.class) {
				columnType = "BOOLEAN";
			} else if (valueType == Float.class) {
				columnType = "REAL";
			} else if (valueType == Integer.class) {
				columnType = "INTEGER";
			}
			else {
				L.e("No corresponding database type found for type " + valueType.getClass().getSimpleName() + ".");
				return;
			}
			
			String sql = "CREATE TABLE " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + 
					"registered DATETIME, " + 
					"value " + columnType + ")";
			
			executeUpdate(sql);
		}
	}
	
	@Override
	public void putValue(Date date, Object value) {
		Connection conn = openConnection();
		
		try {
			PreparedStatement stat = conn.prepareStatement("INSERT INTO " + tableName + "(registered, value) VALUES(?, ?)");
			
			stat.setDate(1, convertToSqlDate(date));
			
			if (value.getClass() != valueType) {
				L.e("Tried to put value of wrong value type in history table " + tableName + ".");
				return;
			}
			
			
			
			//stat.executeUpdate();
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't put value to table " + tableName + " in database.", e);
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public Object getLatestValue() {
		Connection conn = openConnection();
		PreparedStatement stat;
		int value = 0;
		
		try {
			stat = conn.prepareStatement("SELECT value FROM " + tableName + " ORDER BY registered DESC LIMIT 1");
			
			ResultSet rs = stat.executeQuery();
			
			if (rs.next()) {
				value = rs.getInt(1);
			}
			
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't get latest value from table " + tableName + " in database.", e);
		} finally {
			closeConnection(conn);
		}
		
		return value;
	}
}
