package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.tellstick.TellStickSwitch;

public class TellStickSwitchTest {
	private TellStickSwitch device;
	private int assignedId;
	private String assignedName;
	
	@Before
	public void setUp() throws Exception {
		this.assignedId = 1857293476;
		this.assignedName = "My Device #5";
		this.device = new TellStickSwitch("ID123");
		this.device.setName(this.assignedName);
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
		
		Device notTheSame = new TellStickSwitch("323");
		notTheSame.setId(1213131);
		notTheSame.setName("Another Device");
		assertFalse(this.device.equals(notTheSame));
	}
}
