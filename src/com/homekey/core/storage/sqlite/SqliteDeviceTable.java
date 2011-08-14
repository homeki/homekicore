package com.homekey.core.storage.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.homekey.core.log.L;
import com.homekey.core.storage.IDeviceTable;

public class SqliteDeviceTable extends SqliteTable implements IDeviceTable {
	protected SqliteDeviceTable(String databaseName) {
		super(databaseName);
	}
	
	@Override
	public void ensureTable() {
		if (!tableExists("devices")) {
			String sql = "CREATE TABLE devices(id INTEGER PRIMARY KEY AUTOINCREMENT," +
											  "internalid STRING, " +
											  "type STRING, " +
											  "name STRING, " +
											  "added DATETIME, " +
											  "active BOOLEAN)";

			executeUpdate(sql);
		}
	}

	@Override
	public boolean rowExists(String internalId) {
		return getScalar("SELECT COUNT(id) FROM devices WHERE internalid = ?", internalId) > 0;
	}

	@Override
	public int createRow(String internalId, String type) {
		Connection conn = openConnection();
		int id = -1;
		
		try {
			PreparedStatement stat = conn.prepareStatement("INSERT INTO devices(internalid, type, name, added, active) VALUES(?, ?, ?, ?, ?)");
			
			stat.setString(1, internalId);
			stat.setString(2, type);
			stat.setString(3, "");
			stat.setDate(4, new java.sql.Date(new Date().getTime()));
			stat.setBoolean(5, true);
			
			stat.executeUpdate();
			
			id = stat.getGeneratedKeys().getInt(1);
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't insert new device in database.", e);
		}
		finally {
			closeConnection(conn);
		}
		
		return id;
	}

	@Override
	public int getId(String internalId) {
		return (Integer)getField("SELECT id FROM devices WHERE internalid = ?", internalId, Integer.class);
	}
	
	@Override
	public String getInternalId(int id) {
		return (String)getField("SELECT internalid FROM devices WHERE id = ?", id, String.class);
	}
	
	@Override
	public String getType(int id) {
		return (String)getField("SELECT type FROM devices WHERE id = ?", id, String.class);
	}
	
	@Override
	public String getName(int id) {
		return (String)getField("SELECT name FROM devices WHERE id = ?", id, String.class);
	}

	@Override
	public Date getAdded(int id) {
		return (Date)getField("SELECT added FROM devices WHERE id = ?", id, Date.class);
	}

	@Override
	public boolean isActive(int id) {
		return (Boolean)getField("SELECT active FROM devices WHERE id = ?", id, Boolean.class);
	}
	
	@Override
	public void setInternalId(int id, String name) {
		setField("UPDATE devices SET internalid = ? WHERE id = ?", name, id);
	}
	
	@Override
	public void setType(int id, String type) {
		setField("UPDATE devices SET type = ? WHERE id = ?", type, id);
	}

	@Override
	public void setName(int id, String name) {
		setField("UPDATE devices SET name = ? WHERE id = ?", name, id);
	}

	@Override
	public void setActive(int id, boolean active) {
		setField("UPDATE devices SET active = ? WHERE id = ?", active, id);
	}

	@Override
	public void setAdded(int id, Date added) {
		setField("UPDATE devices SET added = ? WHERE id = ?", added, id);
	}
}
