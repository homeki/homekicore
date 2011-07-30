package com.homekey.core.tests;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.mock.MockDimmerDevice;
import com.homekey.core.device.mock.MockSwitchDevice;
import com.homekey.core.http.HttpApi;
import com.homekey.core.main.Monitor;
import com.homekey.core.storage.Database;

public class ApiTest {
	private static final String GET_DEVICES_JSON = TestUtil.getStringFromTextFile("get_devices_json");
	
	private HttpApi api;
	private Database db;
	
	@Before
	public void setUp() throws Exception {
		db = TestUtil.getEmptyTestDatabase();
		
		Monitor mon = new Monitor();
		api = new HttpApi(mon);
		
		mon.addDevice(new MockSwitchDevice("switch1", db)); // id 1 in db
		// TODO: lägg till möjlighet att ändra datum på MockDevice (basklassen) i efterhand (typ dev.mockChangeAddedDate(new Date())
		mon.addDevice(new MockDimmerDevice("dimmer1", db)); // id 2 in db
		//mon.addDevice(new MockTemperatureDevice("dimmer1", db)); // id 3 in db
	}
	
	@After
	public void tearDown() throws Exception {
		db.close();
	}
	
	@Test
	public void testGetDevices() {
		// 
		assertEquals(GET_DEVICES_JSON, api.getDevices());
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
