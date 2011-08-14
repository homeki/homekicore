package com.homekey.core.storage.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.homekey.core.log.L;
import com.homekey.core.storage.IIntegerHistoryTable;

public class SqliteIntegerHistoryTable extends SqliteTable implements IIntegerHistoryTable {
	private final String tableName;
	
	protected SqliteIntegerHistoryTable(String tableName, String databasePath) {
		super(databasePath);
		this.tableName = tableName;
	}

	@Override
	public void ensureTable() {
		if (!tableExists(tableName)) {
			String sql = "CREATE TABLE " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
					  "registered DATETIME, " +
					  "value INTEGER)";

			executeUpdate(sql);
		}
	}

	@Override
	public void putValue(Date date, int value) {
		Connection conn = openConnection();

		try {
			PreparedStatement stat = conn.prepareStatement("INSERT INTO " + tableName + "(registered, value) VALUES(?, ?)");
			
			stat.setDate(1, convertToSqlDate(date));
			stat.setInt(2, value);
			
			stat.executeUpdate();
		} catch (SQLException e) {
			L.e("Couldn't put value to table " + tableName + ".", e);
		}
		finally {
			closeConnection(conn);
		}
	}
}
