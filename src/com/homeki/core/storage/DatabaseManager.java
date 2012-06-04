package com.homeki.core.storage;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.logging.LogFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import com.homeki.core.main.L;

public class DatabaseManager {
	private static final String DATABASE_PATH = "jdbc:postgresql:homeki";
	private static final String DATABASE_USER = "homeki";
	private static final String DATABASE_PASSWORD = "homeki";
	
	private static final String CHANGELOG = "db-changelog.xml";
	
	public void upgrade() throws Exception {
		LogFactory.setLoggingLevel("SEVERE");
		
		Class.forName("org.postgresql.Driver");
		Connection c = DriverManager.getConnection(DATABASE_PATH, DATABASE_USER, DATABASE_PASSWORD);
	
		ResourceAccessor acc = new FileSystemResourceAccessor();
		DatabaseConnection conn = new JdbcConnection(c);
		Liquibase liq = new Liquibase(CHANGELOG, acc, conn);

		liq.getDatabase().setDatabaseChangeLogLockTableName("liquibase_changelog_lock");
		liq.getDatabase().setDatabaseChangeLogTableName("liquibase_changelog");
		
		if (liq.listUnrunChangeSets("").size() == 0)
			return;
		
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
	
	public void dropAll() throws Exception {
		Class.forName("org.postgresql.Driver");
		
		Connection conn = DriverManager.getConnection(DATABASE_PATH, DATABASE_USER, DATABASE_PASSWORD);
		PreparedStatement queryPs = conn.prepareStatement("SELECT tablename FROM pg_tables WHERE tableowner = ?");
		queryPs.setString(1, DATABASE_USER);
		ResultSet rs = queryPs.executeQuery();
		
		while (rs.next()) {
			String tableName = rs.getString(1);
			PreparedStatement dropPs = conn.prepareStatement("DROP TABLE " + tableName + " CASCADE");
			dropPs.execute();
			dropPs.close();
		}
		
		rs.close();
		queryPs.close();
		conn.close();
	}
}
