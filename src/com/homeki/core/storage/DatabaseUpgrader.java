package com.homeki.core.storage;

import java.io.OutputStreamWriter;
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
	
	public void upgrade() throws Exception {
		LogFactory.setLoggingLevel("SEVERE");
		
		registerDriver();
		Connection c = setupConnection();
	
		ResourceAccessor acc = new FileSystemResourceAccessor();
		DatabaseConnection conn = new HsqlConnection(c);
		Liquibase liq = new Liquibase("db-changelog.xml", acc, conn);
		
		if (liq.listUnrunChangeSets("").size() == 0) {
			return;
		}
		
		// log whats about to happen
		StringWriter swriter = new StringWriter();
		liq.reportStatus(false, "", swriter);
		L.i(swriter.toString());
		
		L.i("Starting database upgrade...");
		liq.validate();
		liq.update("");
		L.i("Database upgrade completed.");
		
		if (!conn.isClosed()) {
			L.i("Nope");
			conn.close();
		}
	}
	
	private void registerDriver() {
		try {
	        Class.forName("org.hsqldb.jdbcDriver");
	    } catch (Exception ex) {
	        L.e("Failed to load HSQLDB JDBC driver.", ex);
	        throw new RuntimeException();
	    }		
	}
	
	private Connection setupConnection() {
		Connection c;
		
		try {
			c = DriverManager.getConnection("jdbc:hsqldb:file:db/homeki.db", "sa", "");
		}
		catch (Exception ex) {
			L.e("Failed to connect to embedded HSQLDB.", ex);
			throw new RuntimeException();
		}
		
		return c;
	}
}
