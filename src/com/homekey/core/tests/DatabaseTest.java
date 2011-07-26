package com.homekey.core.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.storage.DataRow;
import com.homekey.core.storage.Database;

public class DatabaseTest {
	Database database;

	@Before
	public void setUp() throws Exception {
		String tmppath = "/tmp/homekeysql555";
		removeDbIfExists(tmppath);
		
//		this.database = new Database(tmppath);
	}

	private void removeDbIfExists(String tmppath) {
		File f = new File(tmppath);
		if(f.exists())
			f.delete();
	}
	
	@After
	public void tearDown() throws Exception {
		database.close();
	}
	
	@Test
	public void testCreateTableFor() {
		Device d = new MockDeviceDimmer("ID123", "My Test Device", false);
	}
	
	@Test
	public void testPutRow() {
		
	}
	
	@Test
	public void testGetNextId() {
	}
}
