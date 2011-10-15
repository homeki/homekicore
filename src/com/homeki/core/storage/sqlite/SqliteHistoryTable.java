package com.homeki.core.storage.sqlite;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.homeki.core.log.L;
import com.homeki.core.storage.DatumPoint;
import com.homeki.core.storage.IHistoryTable;

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
			
			if (valueType == Boolean.class) {
				stat.setBoolean(2, (Boolean)value);
			} else if (valueType == Float.class) {
				stat.setFloat(2, (Float)value);
			} else if (valueType == Integer.class) {
				stat.setInt(2, (Integer)value);
			}
			
			stat.executeUpdate();
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
		Object value = null;
		
		try {
			stat = conn.prepareStatement("SELECT value FROM " + tableName + " ORDER BY registered DESC LIMIT 1");
			
			ResultSet rs = stat.executeQuery();
			
			if (rs.next()) {
				if (valueType == Boolean.class) {
					value = rs.getBoolean(1);
				} else if (valueType == Float.class) {
					value = rs.getFloat(1);
				} else if (valueType == Integer.class) {
					value = rs.getInt(1);
				}
			}
			else {
				// default values
				if (valueType == Boolean.class) {
					value = false;
				} else if (valueType == Float.class) {
					value = 0.0f;
				} else if (valueType == Integer.class) {
					value = 0;
				}
			}
			
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't get latest value from table " + tableName + " in database.", e);
		} finally {
			closeConnection(conn);
		}
		
		return value;
	}
	
	public List<DatumPoint> getValues(Date from, Date to) {
		List<DatumPoint> list = new ArrayList<DatumPoint>();
		Connection conn = openConnection();
		PreparedStatement stat;
		
		try {
			stat = conn.prepareStatement("SELECT registered, value FROM " + tableName + " WHERE registered BETWEEN ? AND ? ORDER BY registered ASC");
			
			stat.setDate(1, convertToSqlDate(from));
			stat.setDate(2, convertToSqlDate(to));
			
			ResultSet rs = stat.executeQuery();
			
			while (rs.next()) {
				if (valueType == Boolean.class) {
					list.add(new DatumPoint(rs.getDate(1), rs.getBoolean(2)));
				} else if (valueType == Float.class) {
					list.add(new DatumPoint(rs.getDate(1), rs.getFloat(2)));
				} else if (valueType == Integer.class) {
					list.add(new DatumPoint(rs.getDate(1), rs.getInt(2)));
				}
			}
			
			stat.close();
		} catch (SQLException e) {
			L.e("Couldn't get values from table " + tableName + " in database.", e);
		} finally {
			closeConnection(conn);
		}
		
		return list;
	}
}