package com.homekey.core.storage.sqlite;

import com.homekey.core.storage.ColumnType;

public class SqliteUtil {
	protected static String merge(String[] columns, int ignoreFromEnd, String delim) {
		String out = "";
		for (int i = 0; i < columns.length - ignoreFromEnd; i++) {
			out += columns[i] + delim;
		}
		return out;
	}
	
	protected static String merge(int length, String delim) {
		String out = "";
		for (int i = 0; i < length; i++) {
			out += delim;
		}
		return out;
	}
	
	protected static String convertToSqlRepresentation(ColumnType type) {
		switch (type) {
		case INTEGER:
			return "INTEGER";
		case DATETIME:
			return "DATETIME";
		case DOUBLE:
			return "REAL";
		case STRING:
			return "TEXT";
		case BOOLEAN:
			return "BOOLEAN";
		default:
			throw new IllegalArgumentException();
		}
	}
}
