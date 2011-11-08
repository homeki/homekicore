package com.homeki.core.storage.sqlite;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.homeki.core.log.L;

public class SqliteTable {
	private final String databaseName;
	
	protected SqliteTable(String databasePath) {
		this.databaseName = databasePath;
	}
	
	protected Connection openConnection() {
		Connection conn = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
			} catch (SQLException e) {
				L.e("Couldn't open database.", e);
			}
		} catch (ClassNotFoundException e) {
			L.e("Couldn't load driver for SQlite.", e);
		}
		
		return conn;
	}
	
	protected void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			L.e("Couldn't close database connection.", e);
		}
	}
	
	protected synchronized Object getField(String sql, Object where, Type returnType) {
		Connection conn = openConnection();
		PreparedStatement stat;
		Object value = null;
		
		try {
			stat = conn.prepareStatement(sql);
			
			addFirstParameter(stat, where);
			
			ResultSet rs = stat.executeQuery();
			
			if (rs.next()) {
				if (returnType == Integer.class) {
					value = rs.getInt(1);
				}
				else if (returnType == String.class) {
					value = rs.getString(1);
				}
				else if (returnType == Float.class) {
					value = rs.getFloat(1);
				}
				else if (returnType == Date.class) {
					value = rs.getDate(1);
				}
				else if (returnType == Boolean.class) {
					value = rs.getBoolean(1);
				}
			}
			
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't get " + returnType.getClass().getSimpleName() + " value from database.", e);
		} finally {
			closeConnection(conn);
		}
		
		return value;
	}
	
	protected synchronized void setField(String sql, Object value, int id) {
		Connection conn = openConnection();
		
		try {
			PreparedStatement stat = conn.prepareStatement(sql);
			addFirstParameter(stat, value);
			stat.setInt(2, id);
			stat.executeUpdate();
			stat.close();
		} catch (SQLException ex) {
			L.e("Couldn't update row in database.", ex);
		}
		finally {
			closeConnection(conn);
		}
	}
	
	protected int getScalar(String sql, Object where) {
		Connection conn = openConnection();
		PreparedStatement stat;
		int count = 0;
		
		try {
			stat = conn.prepareStatement(sql);
			
			addFirstParameter(stat, where);
			
			ResultSet rs = stat.executeQuery();
			rs.next();
			count = rs.getInt(1);
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't get scalar from row in database.", e);
		}
		finally {
			closeConnection(conn);
		}
		
		return count;
	}
	
	protected void addFirstParameter(PreparedStatement prep, Object value) throws SQLException {
		if (value instanceof Integer) {
			prep.setInt(1, (Integer)value);
		}
		else if (value instanceof String) {
			prep.setString(1, (String)value);
		}
		else if (value instanceof Float) {
			prep.setFloat(1, (Float)value);
		}
		else if (value instanceof java.util.Date) {
			prep.setDate(1, convertToSqlDate((java.util.Date)value));
		}
		else if (value instanceof Boolean) {
			prep.setBoolean(1, (Boolean)value);
		}
	}
	
	protected boolean tableExists(String tableName) {
		return getScalar("SELECT COUNT(name) FROM sqlite_master WHERE type = 'table' AND name = ?", tableName) > 0;
	}
	
	protected void executeUpdate(String sql) {
		Connection conn = openConnection();
		Statement stat;
		
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't execute UPDATE SQL.", e);
		}
		finally {
			closeConnection(conn);
		}
	}
	
	protected java.sql.Date convertToSqlDate(java.util.Date date) {
		java.util.Date utilDate = (java.util.Date)date;
		return new java.sql.Date(utilDate.getTime());
	}
}
