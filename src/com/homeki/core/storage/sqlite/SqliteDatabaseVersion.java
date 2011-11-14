package com.homeki.core.storage.sqlite;

public class SqliteDatabaseVersion extends SqliteTable implements Comparable<SqliteDatabaseVersion> {
	private int major;
	private int minor;
	private int micro;
	
	protected SqliteDatabaseVersion(String version, String databasePath) {
		super(databasePath);
		
		String[] parts = version.split("\\.");
		
		major = Integer.parseInt(parts[0]);
		minor = Integer.parseInt(parts[1]);
		micro = Integer.parseInt(parts[2]);
	}
	
	@Override
	public String toString() {
		return Integer.toString(major) + "." + Integer.toString(minor) + "." + Integer.toString(micro);
	}

	@Override
	public int compareTo(SqliteDatabaseVersion o) {
		if (major == o.major) {
			if (minor == o.minor) {
				if (micro == o.micro) {
					return 0;
				} else {
					return micro - o.micro;
				}
			} else {
				return minor - o.minor;
			}
		} else {
			return major - o.major;
		}
	}
	
	public void run() {
		// override this!
	}
}
