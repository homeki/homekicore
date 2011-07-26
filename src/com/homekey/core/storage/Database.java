package com.homekey.core.storage;

import java.sql.*;

import com.homekey.core.device.Device;
import com.homekey.core.device.onewire.OneWireTemperatureSensor;

public class Database {
	private final String DATABASE_NAME = "homekey.db";
	private final String SENSOR_PREFIX = "D_";
	
	private Connection conn;
	
	public Database() {
		init();
		open();
	}
	
	public void createTable(Device device) {
		DatabaseTable table = device.getTableDesign();
		String tableName = SENSOR_PREFIX + device.getClass().getSimpleName() + "_" + device.getId();
		String sql = "CREATE TABLE " + tableName + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, ";
		
		for (int i = 0; i < table.getColumnCount(); i++) {
			sql += table.getName(i) + " " + convertToSqlRepresentation(table.getType(i)) + ", ";
		}
		
		sql = sql.substring(0, sql.length() - 2) + ");";
		
		Statement stat;
		
		try {
			stat = conn.createStatement();
			stat.executeUpdate(sql);
		}
		catch (SQLException ex) {
			System.err.println("createTable(): Couldn't execute SQL.");
		}
	}
	
	public void putRow(int id, DataRow row) {
		
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
		default:
			throw new IllegalArgumentException();
		}
	}
	
	private void init() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException ex) {
			System.err.println("ensureDatabase(): Couldn't load SQlite JDBC driver.");
		}
	}
	
	private void open() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		} catch (SQLException ex) {
			System.err.println("ensureDatabase(): Couldn't open database named " + DATABASE_NAME + ".");
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException ex) {
			System.err.println("ensureDatabase(): Couldn't close database connection.");
		}
	}
	
	private void ensureDatabase() {
		/*
		 * Statement stat = conn.createStatement();
		 * stat.executeUpdate("drop table if exists people;");
		 * stat.executeUpdate("create table people (name, occupation);");
		 * 
		 * PreparedStatement prep =
		 * conn.prepareStatement("insert into people values (?, ?);");
		 * 
		 * prep.setString(1, "Gandhi"); prep.setString(2, "politics");
		 * prep.addBatch(); prep.setString(1, "Turing"); prep.setString(2,
		 * "computers"); prep.addBatch(); prep.setString(1, "Wittgenstein");
		 * prep.setString(2, "smartypants"); prep.addBatch();
		 * 
		 * conn.setAutoCommit(false); prep.executeBatch();
		 * conn.setAutoCommit(true);
		 * 
		 * ResultSet rs = stat.executeQuery("select * from people;"); while
		 * (rs.next()) { System.out.println("name = " + rs.getString("name"));
		 * System.out.println("job = " + rs.getString("occupation")); }
		 * rs.close(); conn.close();
		 */
	}
	
	public int getNextId() {
		throw new UnsupportedOperationException();
	}
}
