package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDevice;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.storage.Database;

public class DeviceTest {
	private Database db;
	private Device device;
	
	@Before
	public void setUp() throws Exception {
		db = TestUtil.getEmptyTestDatabase();
		device = new MockDevice("mock1", db);
		device.setName("My MockDevice #1");
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testHashCode() {
		assertEquals("Hashcode must be equal to ID.", device.hashCode(), device.getId());
	}
	
	@Test
	public void testGetName() {
		assertEquals("Name is not the assigned name.", device.getName(), "My MockDevice #1");
	}
	
	@Test
	public void testEquals() {
		assertTrue(device.equals(device));
		assertTrue(((Device) device).equals(device));
		Device notTheSame = new MockDeviceDimmer("notTheSame1", db);
		notTheSame.setName("NotTheSame");
		assertFalse(device.equals(notTheSame));
	}
}
