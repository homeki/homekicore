package com.homekey.core.storage;

import java.sql.*;
import java.util.Calendar;

import com.homekey.core.device.Device;

public class Database {
	private final String DEFAULT_DATABASE_NAME = "homekey.db";
	private final String SENSOR_PREFIX = "D_";
	
	private String databaseName;
	private Connection conn;
	
	public Database() {
		databaseName = DEFAULT_DATABASE_NAME;
		init();
		open();
		ensureSystemTables();
	}
	
	public Database(String name) {
		databaseName = name;
		init();
		open();
		ensureSystemTables();
	}
	
	public boolean deviceExists(Device device) {
		return executeScalar("SELECT COUNT(internalid) FROM devices WHERE internalid = '" + device.getInternalId() + "';") > 0;
	}
	
	public void registerDevice(Device device) {
		if (deviceExists(device)) {
			throw new IllegalArgumentException("A device with the specified internal id already exists.");
		}
		
		addDevice(device);
		createTableFor(device);
	}
	
	public void putRow(int id, DataRow row) {
		
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException ex) {
			System.err.println("close(): Couldn't close database connection.");
		}
	}
	
	public int getNextId() {
		return -1;
	}
	
	private void addDevice(Device device) {
		PreparedStatement stat;
		
		try {
			stat = conn.prepareStatement("INSERT INTO devices(internalid, type, name, added, active) VALUES(?, ?, ?, ?, ?);");
			stat.setString(1, device.getInternalId());
			stat.setString(2, device.getClass().getSimpleName());
			stat.setString(3, device.getName());
			stat.setDate(4, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			stat.setBoolean(5, true);
			stat.execute();
		}
		catch (SQLException ex) {
			System.err.println("addDevice(): Couldn't execute SQL.");
		}
	}
	
	private void createTableFor(Device device) {
		DatabaseTable table = device.getTableDesign();
		String tableName = SENSOR_PREFIX + device.getClass().getSimpleName() + "_" + device.getId();
		String sql = "CREATE TABLE " + tableName + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, ";
		
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
	
	private void init() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
			System.err.println("init(): Couldn't load SQlite JDBC driver.");
		}
	}
	
	private void open() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
		} catch (SQLException ex) {
			System.err.println("open(): Couldn't open database named " + databaseName + ".");
		}
	}
	
	private void ensureSystemTables() {
		if (!tableExists("devices")) {
			executeUpdate("CREATE TABLE devices(id INTEGER PRIMARY KEY AUTOINCREMENT, internalid TEXT, type TEXT, name TEXT, added DATETIME, active BOOLEAN)");
		}
	}
	
	private boolean tableExists(String tableName) {
		return executeScalar("SELECT COUNT(name) FROM sqlite_master WHERE type='table' AND name='" + tableName + "';") > 0;
	}
	
	private void executeUpdate(String sql) {
		Statement stat;
		
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);
		}
		catch (SQLException ex) {
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
		}
		catch (SQLException ex) {
			System.err.println("executeScalar(): Couldn't execute SQL query.");
		}
		
		return count;
	}
}
