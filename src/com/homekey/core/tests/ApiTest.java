package com.homekey.core.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.http.HttpApi;
import com.homekey.core.main.Monitor;
import com.homekey.core.storage.Database;

public class ApiTest {
	private HttpApi api;
	private Database db;
	
	@Before
	public void setUp() throws Exception {
		db = TestUtil.getEmptyTestDatabase();
		
		Monitor mon = new Monitor();
		
		
	}
	
	@After
	public void tearDown() throws Exception {}
	
	@Test
	public void testGetDevices() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSwitchOn() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSwitchOff() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDim() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetData() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetStatus() {
		fail("Not yet implemented");
	}
	
}
