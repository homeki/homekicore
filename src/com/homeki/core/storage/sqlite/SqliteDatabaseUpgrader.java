package com.homeki.core.storage.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.log.L;

public class SqliteDatabaseUpgrader extends SqliteTable {
	public abstract class Version {
		public String version;
		
		public Version(String version) {
			this.version = version;
		}
		
		public abstract void run();
	}
	
	private List<Version> versions;
	private String fromVersion;
	
	public SqliteDatabaseUpgrader(String databasePath, String fromVersion) {
		super(databasePath);
		this.versions = new ArrayList<Version>();
		this.fromVersion = fromVersion;
	}
	
	private void to0_0_9() {
		versions.add(new Version("0.0.9") {
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
		});
	}
	
	public void execute() {
		to0_0_9();
		
		boolean myVersionFound = false;
		
		for (int i = 0; i < versions.size(); i++) {
			Version v = versions.get(i);
			
			if (v.version.equals(fromVersion))
				myVersionFound = true;
			
			if (myVersionFound) {
				v.run();
				L.i("Upgraded database to version " + v.version + ".");
			}
		}
	}
}
