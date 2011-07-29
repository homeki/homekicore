package com.homekey.core.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDeviceDimmer;

public class DeviceTest {
	private MockDeviceDimmer device;
	private String assignedName;
	@Before
	public void setUp() throws Exception {
		this.assignedName = "My Device #5";
		this.device = new MockDeviceDimmer("ID123");
		this.device.setName(this.assignedName);
		//this.device.setId(this.assignedId);
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testHashCode() {
		assertEquals("Hashcode must be equal to id." ,this.device.hashCode(), this.device.getId());
	}
	
	@Test
	public void testGetName() {
		assertEquals("Name is not the assigned name.", this.device.getName(), this.assignedName);
	}
	
	
	@Test
	public void testEqualsObject() {
		assertTrue(this.device.equals(this.device));
		assertTrue(((Device)this.device).equals(this.device));
		Device notTheSame = new MockDeviceDimmer("323");
		//notTheSame.setId(1213131);
		notTheSame.setName("Another Device");
		assertFalse(this.device.equals(notTheSame));
	}
}
