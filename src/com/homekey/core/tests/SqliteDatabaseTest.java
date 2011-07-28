package com.homekey.core.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDeviceSwitcher;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.impl.SqliteDatabase;

public class SqliteDatabaseTest {
	private final String TMP_DATABASE_PATH = "/tmp/homekeysql555.db";
	
	private Database db;
	private Device dev1;
	
	@Before
	public void setUp() throws Exception {
		removeDbIfExists(TMP_DATABASE_PATH);
		
		db = new SqliteDatabase(TMP_DATABASE_PATH);
		dev1 = new MockDeviceSwitcher("int123", false);
	}
	
	@After
	public void tearDown() throws Exception {
		db.close();
	}
	
	@Test
	public void testEnsureDevice() {
		// first time ensured, should be added to db and get first id 1
		dev1.setName("FirstDevice");
		db.ensureDevice(dev1);
		assertEquals(1, dev1.getId());
		
		// new device with same internal id should be loaded from db
		Device dev2 = new MockDeviceSwitcher("int123", false);
		db.ensureDevice(dev2);
		assertEquals("FirstDevice", dev2.getName());
	}
	
	@Test
	public void testPutRow() {
		
	}
	
	private void removeDbIfExists(String tmppath) {
		File f = new File(tmppath);
		if (f.exists()) {
			f.delete();
		}
	}
}
