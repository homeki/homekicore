package com.homekey.core.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.mock.MockDeviceDimmer;

public class DeviceTest {
	
	private MockDeviceDimmer device;
	private int assignedId;
	private String assignedName;
	@Before
	public void setUp() throws Exception {
		this.assignedId = 1857293476;
		this.assignedName = "My Device #5";
		this.device = new MockDeviceDimmer("ID123", this.assignedName+"", false);
		this.device.setId(this.assignedId);
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
		assertEquals("Id is not the assigned id.", this.device.getName(), this.assignedName);
	}
	
	@Test
	public void testGetId() {
		assertEquals("Id is not the assigned id.", this.device.getId(), this.assignedId);
	}
	
	@Test
	public void testEqualsObject() {
		assertTrue(this.device.equals(this.device));
		assertTrue(((Device)this.device).equals(this.device));
		Device notTheSame = new MockDeviceDimmer("323", "Another Device", false);
		notTheSame.setId(1213131);
		assertFalse(this.device.equals(notTheSame));
	}
}
