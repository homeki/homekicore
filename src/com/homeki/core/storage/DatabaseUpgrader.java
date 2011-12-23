package com.homeki.core.storage;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.HsqlConnection;
import liquibase.logging.LogFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import com.homeki.core.main.L;

public class DatabaseUpgrader {
	private static final String DATABASE_PATH = "jdbc:hsqldb:file:db/homeki.db";
	private static final String DATABASE_USER = "sa";
	private static final String DATABASE_PASSWORD = "";
	private static final String CHANGELOG = "db-changelog.xml";
	
	public void upgrade() throws Exception {
		LogFactory.setLoggingLevel("SEVERE");
		
		Class.forName("org.hsqldb.jdbcDriver");
		Connection c = DriverManager.getConnection(DATABASE_PATH, DATABASE_USER, DATABASE_PASSWORD);
	
		ResourceAccessor acc = new FileSystemResourceAccessor();
		DatabaseConnection conn = new HsqlConnection(c);
		Liquibase liq = new Liquibase(CHANGELOG, acc, conn);
		
		if (liq.listUnrunChangeSets("").size() == 0) {
			return;
		}
		
		L.i("Starting database upgrade...");
		
		// log what's about to happen
		StringWriter swriter = new StringWriter();
		liq.reportStatus(false, "", swriter);
		L.ii(swriter.toString());
		
		// execute the changelog
		liq.validate();
		L.i("Applying changesets...");
		liq.update("");
		L.i("Changesets applied, database upgrade completed.");
		
		if (!conn.isClosed())
			conn.close();
	}
}
