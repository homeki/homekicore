package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDevice;
import com.homekey.core.device.mock.MockDimmerDevice;
import com.homekey.core.storage.ITableFactory;

public class DeviceTest {
	private static final String INTERNAL_ID = "mock1";
	private static final String NAME = "My MockDevice #1";
	private static Date testStart = new Date();
	
	private ITableFactory factory;
	private Device device;
	
	@Before
	public void setUp() throws Exception {
		factory = TestUtil.getEmptyTestTableFactory();
		device = new MockDevice(INTERNAL_ID, factory);
		device.setName(NAME);
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
		assertEquals("Name is not the assigned name.", NAME, device.getName());
	}
	
	@Test
	public void testGetInternalId() {
		assertEquals("Internal ID is not the assigned internal ID.", INTERNAL_ID, device.getInternalId());
	}
	
	@Test
	public void testEquals() {
		assertTrue(device.equals(device));
		assertTrue(((Device) device).equals(device));
		Device notTheSame = new MockDimmerDevice("notTheSame1", factory);
		notTheSame.setName("NotTheSame");
		assertFalse(device.equals(notTheSame));
	}
	
	@Test
	public void testGetAdded() {
		Date d = device.getAdded();
		Date now = new Date();
		assertTrue(now.after(d));
		assertTrue(testStart.before(d));
	}
	
	@Test
	public void testActive() {
		device.setActive(true);
		assertTrue(device.isActive());
		device.setActive(false);
		assertTrue(!device.isActive());
	}
}
