package com.homekey.core.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.command.CommandsThread;
import com.homekey.core.command.DimDeviceCommand;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.device.Device;
import com.homekey.core.device.Dimmable;
import com.homekey.core.device.Switchable;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.device.mock.MockDeviceSwitcher;
import com.homekey.core.main.Monitor;
import com.homekey.core.main.ThreadMaster;
import com.homekey.core.storage.Database;

public class MonitorTest {
	private MockDeviceSwitcher dev1;
	private MockDeviceDimmer dev2;
	private Switchable switchable;
	private Dimmable dimmable;
	
	@Before
	public void setUp() throws Exception {
		dev1 = new MockDeviceSwitcher("DA", "My MockDevice #1", true);
		dev2 = new MockDeviceDimmer("DDD", "My MockDevice #2", true);
		dev1.setId(10);
		dev2.setId(20);
		switchable = (Switchable) dev1;
		dimmable = (Dimmable) dev2;
		/*
		 * // Create devices Device dev1 = new MockDeviceSwitcher("DA",
		 * "My MockDevice #1", true); Device dev2 = new MockDeviceDimmer("DDD",
		 * "My MockDevice #2", true); // Register devices with db
		 * b.ensureDevice(dev1); b.ensureDevice(dev2); // Add devices
		 * m.forceAddDevice(dev1); m.forceAddDevice(dev2); // Query devices by
		 * ID Switchable switchable = (Switchable) m.getDevice(dev1.getId());
		 * Dimmable dimmable = (Dimmable) m.getDevice(dev2.getId()); // Create
		 * commands DimDeviceCommand ddc = new DimDeviceCommand(dimmable, 50);
		 * SwitchDeviceCommand sdcOn = new SwitchDeviceCommand(switchable,
		 * true); SwitchDeviceCommand sdcOff = new
		 * SwitchDeviceCommand(switchable, false);
		 * 
		 * // Post commands m.post(sdcOn); m.post(ddc); m.post(sdcOff);
		 * 
		 * sdcOff.getResult(); ddc.getResult(); sdcOn.getResult();
		 */
	}
	
	@After
	public void tearDown() throws Exception {}
	
	@Test
	public void testGetDevice() {
		Monitor m = new Monitor();
		// getDevice should return null for all getDevice
		assertEquals(m.getDevice(dev1.getId()), null);
		assertEquals(m.getDevice(dev2.getId()), null);
		assertEquals(m.getDevice(12345), null);
		
		m.forceAddDevice(dev1);
		m.forceAddDevice(dev2);
		
		// Return correct device
		assertEquals(m.getDevice(dev1.getId()), dev1);
		assertEquals(m.getDevice(dev2.getId()), dev2);
		assertNotSame(m.getDevice(dev2.getId()), dev1);
		assertNotSame(m.getDevice(dev1.getId()), dev2);
		
	}
	
	@Test
	public void testTurnOnOff() {
		Monitor m = new Monitor();
		CommandsThread ct = new CommandsThread(m);
		ct.start();
		m.forceAddDevice(dev1);
		m.forceAddDevice(dev2);
		for (int i = 0; i < 10; i++) {
			assertTrue(new SwitchDeviceCommand(dev1, true).postAndWaitForResult(m));
			assertTrue(dev1.getValue().contains("on"));
			assertTrue(new SwitchDeviceCommand(dev1, false).postAndWaitForResult(m));
			assertTrue(dev1.getValue().contains("off"));
			assertTrue(new SwitchDeviceCommand(dev2, true).postAndWaitForResult(m));
			assertTrue("SwitchDevice should be 255 when on, but it is " + dev2.getValue(), dev2.getValue().equals("255"));
			assertTrue(new SwitchDeviceCommand(dev2, false).postAndWaitForResult(m));
			assertTrue("SwitchDevice should be 0 when off, but it is " + dev2.getValue(),dev2.getValue().equals("0"));
		}
	}
	
	@Test
	public void testTurnOff() {
		
	}
	
	@Test
	public void testGetDevices() {
		
	}
	
	@Test
	public void testTakeCommand() {
		
	}
	
}
