package com.homekey.core.storage.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.homekey.core.device.Device;
import com.homekey.core.storage.ColumnType;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.DatabaseTable;

public class SqliteDatabase extends Database {
	private Connection conn;
	
	public SqliteDatabase(String databaseName) {
		super(databaseName);
	}
	
	public SqliteDatabase() {
		super();
	}
	
	@Override
	public void close() {
		try {
			System.out.println("Shutting down database");
			conn.close();
		} catch (SQLException ex) {
			System.err.println("close(): Couldn't close database connection.");
		}
	}
	
	@Override
	public void putRow(Device device, Object[] values) {
		DatabaseTable table = device.getTableDesign();
		String tableName = getTableName(device);
		String sql = "INSERT INTO " + tableName + "(";
		
		// generate sql command from table design
		for (int i = 0; i < table.getColumnCount(); i++) {
			sql += table.getName(i) + ", ";
		}
		sql = sql.substring(0, sql.length() - 2) + ") VALUES(";
		for (int i = 0; i < table.getColumnCount(); i++) {
			sql += "?, ";
		}
		sql = sql.substring(0, sql.length() - 2) + ");";
		
		PreparedStatement stat;
		
		try {
			stat = conn.prepareStatement(sql);
			
			// add parameters to sql command
			for (int i = 0; i < table.getColumnCount(); i++) {
				switch (table.getType(i)) {
				case String:
					stat.setString(i + 1, (String) values[i]);
					break;
				case Boolean:
					stat.setBoolean(i + 1, (Boolean) values[i]);
					break;
				case Integer:
					stat.setInt(i + 1, (Integer) values[i]);
					break;
				case Float:
					stat.setFloat(i + 1, (Float) values[i]);
					break;
				case DateTime:
					java.util.Date javaDate = (java.util.Date) values[i];
					java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
					stat.setDate(i + 1, sqlDate);
					break;
				default:
					throw new IllegalArgumentException();
				}
			}
			
			stat.execute();
		} catch (SQLException ex) {
			System.err.println("putRow(): Couldn't execute INSERT SQL.");
		}
	}
	
	@Override
	protected boolean tableExists(String name) {
		return executeScalar("SELECT COUNT(name) FROM sqlite_master WHERE type='table' AND name='" + name + "';") > 0;
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
	protected void addDevice(Device device) {
		PreparedStatement stat;
		
		try {
			stat = conn.prepareStatement("INSERT INTO devices(internalid, type, name, added, active) VALUES(?, ?, ?, ?, ?);");
			stat.setString(1, device.getInternalId());
			stat.setString(2, device.getClass().getSimpleName());
			stat.setString(3, device.getName());
			stat.setDate(4, new java.sql.Date(device.getAdded().getTime()));
			stat.setBoolean(5, device.isActive());
			stat.execute();
		} catch (SQLException ex) {
			System.err.println("addDevice(): Couldn't execute INSERT SQL.");
		}
		
		device.setId(executeScalar("SELECT id FROM devices WHERE internalid = '" + device.getInternalId() + "';"));
	}
	
	@Override
	protected boolean deviceExists(Device device) {
		return executeScalar("SELECT COUNT(internalid) FROM devices WHERE internalid ='" + device.getInternalId() + "';") > 0;
	}
	
	@Override
	protected void createTable(String name, DatabaseTable table) {
		String sql = "CREATE TABLE " + name + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, ";
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			sql += table.getName(i) + " " + convertToSqlRepresentation(table.getType(i)) + ", ";
		}
		
		sql = sql.substring(0, sql.length() - 2) + ");";
		executeUpdate(sql);
	}
	
	private String convertToSqlRepresentation(ColumnType type) {
		switch (type) {
		case Integer:
			return "INTEGER";
		case DateTime:
			return "DATETIME";
		case Float:
			return "REAL";
		case String:
			return "TEXT";
		case Boolean:
			return "BOOLEAN";
		default:
			throw new IllegalArgumentException();
		}
	}
	
	protected void loadDevice(Device device) {
		Statement stat;
		
		try {
			stat = conn.createStatement();
			
			ResultSet rs = stat.executeQuery("SELECT * FROM devices WHERE internalid = '" + device.getInternalId() + "';");
			rs.next();
			
			try {
				if (!rs.getString("type").equals(device.getClass().getSimpleName())) {
					throw new ClassCastException("The device sent to function was of type " + device.getClass().getSimpleName() + ", but corresponding object in database was of type " + rs.getString("type") + ".");
				}
				
				device.setId(rs.getInt("id"));
				device.setName(rs.getString("name"));
				device.setActive(rs.getBoolean("active"));
			} finally {
				rs.close();
			}
		} catch (SQLException ex) {
			System.err.println("loadDevice(): Couldn't execute SQL query.");
		}
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
