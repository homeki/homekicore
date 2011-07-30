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
	private String assignedName;
	private String assignedInternalId;
	
	@Before
	public void setUp() throws Exception {
		assignedInternalId = "mock1";
		assignedName = "My Device #5";
		device = new TellStickSwitch(assignedInternalId, null);
		device.setName(assignedName);
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
	public void testEqualsObject() {
		assertTrue(this.device.equals(this.device));
		assertTrue(((Device)this.device).equals(this.device));
		
		Device notTheSame = new TellStickSwitch("323", null);
		//notTheSame.setId(1213131);
		notTheSame.setName("Another Device");
		assertFalse(this.device.equals(notTheSame));
	}
}
