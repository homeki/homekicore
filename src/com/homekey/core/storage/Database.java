package com.homekey.core.storage;

import java.sql.*;

import com.homekey.core.device.Device;
import com.homekey.core.device.onewire.OneWireTemperatureSensor;

public class Database {
	private final String DATABASE_NAME = "homeKey.db";
	
	private Connection conn;
	
	public Database() {
		init();
		open();
	}
	
	public void createTable(Device device) {
		if (device instanceof OneWireTemperatureSensor) {
			
		}
	}
	
	public void putRow(int id, DataRow row) {
		
	}
	
	private void init() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception ex) {
			System.out.println("ensureDatabase(): Couldn't load SQlite JDBC driver.");
		}
	}
	
	private void open() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
		} catch (Exception ex) {
			System.out.println("ensureDatabase(): Couldn't open database named " + DATABASE_NAME + ".");
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (Exception ex) {
			System.out.println("ensureDatabase(): Couldn't close database connection.");
		}
	}
	
	private void ensureDatabase() {
		/*
		Statement stat = conn.createStatement();
		stat.executeUpdate("drop table if exists people;");
		stat.executeUpdate("create table people (name, occupation);");
		
		PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?);");
		
		prep.setString(1, "Gandhi");
		prep.setString(2, "politics");
		prep.addBatch();
		prep.setString(1, "Turing");
		prep.setString(2, "computers");
		prep.addBatch();
		prep.setString(1, "Wittgenstein");
		prep.setString(2, "smartypants");
		prep.addBatch();
		
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
		
		ResultSet rs = stat.executeQuery("select * from people;");
		while (rs.next()) {
			System.out.println("name = " + rs.getString("name"));
			System.out.println("job = " + rs.getString("occupation"));
		}
		rs.close();
		conn.close();
		*/
	}
	
	public int getNextId() {
		throw new UnsupportedOperationException();
	}
}
