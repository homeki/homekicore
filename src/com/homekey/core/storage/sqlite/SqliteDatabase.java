package com.homekey.core.storage.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.homekey.core.Logs;
import com.homekey.core.log.L;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class SqliteDatabase extends Database {
	private static Database database;
	
	private Connection conn;
	
	public static Database getInstance() {
		return getInstance(DEFAULT_DATABASE_NAME);
	}
	
	public static Database getInstance(String databaseName) {
		if (database == null) {
			database = new SqliteDatabase();
		}
		
		return database;
	}
	
	private SqliteDatabase(String databaseName) {
		super(databaseName);
	}
	
	private SqliteDatabase() {
		super();
	}
	
	@Override
	public void close() {
		try {
			L.getLogger(Logs.HOMEKEY).log("Shutting down database");
			conn.close();
		} catch (SQLException ex) {
			System.err.println("close(): Couldn't close database connection.");
		}
	}
	
	@Override
	public void addRow(String table, String[] columns, Object[] values) {
		String sql = "INSERT INTO " + table + "(";
		
		sql += SqliteUtil.merge(columns, 0, ", ");
		sql = sql.substring(0, sql.length() - 2) + ") VALUES(";
		sql += SqliteUtil.merge(columns.length, "?, ");
		sql = sql.substring(0, sql.length() - 2) + ");";
		
		executeParametrized(columns, values, sql);
	}
	
	@Override
	public void updateRow(String table, String[] columns, Object[] values) {
		String sql = "UPDATE " + table + " SET ";
		
		sql = sql + SqliteUtil.merge(columns, 1, " = ?, ");
		
		sql = sql.substring(0, sql.length() - 2) + " WHERE " + columns[columns.length - 1] + " = ?;";
		
		executeParametrized(columns, values, sql);
	}
	
	
	private void executeParametrized(String[] columns, Object[] values, String sql) {
		PreparedStatement stat;
		
		try {
			stat = conn.prepareStatement(sql);
			
			for (int i = 0; i < columns.length; i++) {
				setParameter(values[i], stat,i+1);
			}
			
			stat.execute();
		} catch (SQLException ex) {
			System.err.println("updateRow(): Couldn't execute UPDATE SQL.");
		}
	}
	
	private void setParameter(Object value, PreparedStatement stat, int i) throws SQLException {
		if (value instanceof String) {
			stat.setString(i, (String) value);
		} else if (value instanceof Boolean) {
			stat.setBoolean(i , (Boolean) value);
		} else if (value instanceof Integer) {
			stat.setInt(i, (Integer) value);
		} else if (value instanceof Float) {
			stat.setFloat(i , (Float) value);
		} else if (value instanceof java.util.Date) {
			java.util.Date javaDate = (java.util.Date) value;
			java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
			stat.setDate(i , sqlDate);
		}
	}
	
	@Override
	public Object[] getRow(String table, String[] columns, Object value) {
		Object[] values = null;
		String sql = "SELECT ";
		sql += SqliteUtil.merge(columns, 1, ", ");
		sql = sql.substring(0, sql.length() - 2) + " FROM " + table + " WHERE " + columns[columns.length - 1] + " = ?;";
		
		PreparedStatement stat;
		
		try {
			stat = conn.prepareStatement(sql);
			setParameter(value, stat, 1);
			ResultSet rs = stat.executeQuery();
			
			if (rs.next()) {
				values = new Object[columns.length - 1];
				try {
					for (int i = 0; i < columns.length - 1; i++) {
						values[i] = rs.getObject(i + 1);
					}
				} finally {
					rs.close();
				}
			}
		} catch (SQLException ex) {
			L.e("Couldn't execute SQL query.");
		}
		
		return values;
	}
	
	@Override
	public boolean tableExists(String name) {
		return executeScalar("SELECT COUNT(name) FROM sqlite_master WHERE type='table' AND name='" + name + "';") > 0;
	}
	
	@Override
	public String getFieldAsString(String table, String[] columns, Object value) {
		Object[] fields = getRow(table, columns, value);
		return (String) fields[0];
	}
	
	@Override
	public boolean getFieldAsBoolean(String table, String[] columns, Object value) {
		Object[] fields = getRow(table, columns, value);
		return (Integer) fields[0] != 0;
	}
	
	@Override
	public int getFieldAsInteger(String table, String[] columns, Object value) {
		Object[] fields = getRow(table, columns, value);
		return (Integer) fields[0];
	}
	
	@Override
	public Date getFieldAsDate(String table, String[] columns, Object value) {
		Object[] fields = getRow(table, columns, value);
		return new Date((Long) fields[0]);
	}
	
	@Override
	public Object getField(String table, String column, String orderByColumn) {
		Object value = null;
		String sql = "SELECT LIMIT 1 " + column + " FROM " + table + " ORDER BY " + orderByColumn + " DESC";
		
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			
			rs.next();
			try {
				value = rs.getObject(1);
			} finally {
				rs.close();
			}
		} catch (SQLException ex) {
			System.err.println("loadDevice(): Couldn't execute SQL query.");
		}
		
		return value;
	}
	
	@Override
	protected void open() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
			System.err.println("open(): Couldn't load SQlite JDBC driver.");
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
		} catch (SQLException ex) {
			System.err.println("open(): Couldn't open database named " + databaseName + ".");
		}
	}
	
	@Override
	public void createTable(String name, DatabaseTable table) {
		String sql = "CREATE TABLE " + name + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, ";
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			sql += table.getName(i) + " " + SqliteUtil.convertToSqlRepresentation(table.getType(i)) + ", ";
		}
		
		sql = sql.substring(0, sql.length() - 2) + ");";
		executeUpdate(sql);
	}
	
	private void executeUpdate(String sql) {
		Statement stat;
		
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException ex) {
			System.err.println("executeUpdate(): Couldn't execute SQL command.");
		}
	}
	
	private int executeScalar(String sql) {
		Statement stat;
		int count = 0;
		
		try {
			stat = conn.createStatement();
			
			ResultSet rs = stat.executeQuery(sql);
			rs.next();
			count = rs.getInt(1);
			rs.close();
		} catch (SQLException ex) {
			System.err.println("executeScalar(): Couldn't execute SQL query.");
		}
		
		return count;
	}
}
