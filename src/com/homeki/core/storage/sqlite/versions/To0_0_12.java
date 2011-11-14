package com.homeki.core.storage.sqlite.versions;

import com.homeki.core.storage.sqlite.SqliteDatabaseVersion;

public class To0_0_12 extends SqliteDatabaseVersion {
	public To0_0_12(String databasePath) {
		super("0.0.12", databasePath);
	}

	@Override
	public void run() {
		executeUpdate("BEGIN TRANSACTION;" +
				"CREATE TABLE t_devices(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				  "internalid STRING, " +
				  "type STRING, " +
				  "name STRING, " + 
				  "added DATETIME, " +
				  "active BOOLEAN); " +
				"INSERT INTO t_devices SELECT id, internalid, type, name, added, active FROM devices;" +
				"DROP TABLE devices;" +
				"CREATE TABLE devices(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				  "internalid STRING, " +
				  "type STRING, " +
				  "name STRING, " +
				  "description STRING, " +
				  "added DATETIME, " +
				  "active BOOLEAN); " +
				"INSERT INTO devices SELECT id, internalid, type, name, '', added, active FROM t_devices;" +
				"DROP TABLE t_devices;" +
				"COMMIT;");
	}
}
