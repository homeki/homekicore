package com.homekey.core.tests;

import java.io.File;

import org.junit.After;
import org.junit.Before;

public class SqliteDatabaseTest {
	private final String TMP_DATABASE_PATH = "/tmp/homekeysql555.db";
	
	@Before
	public void setUp() throws Exception {
		removeDbIfExists(TMP_DATABASE_PATH);
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	private void removeDbIfExists(String tmppath) {
		File f = new File(tmppath);
		if (f.exists()) {
			f.delete();
		}
	}
}
