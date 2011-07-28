package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.CommandThread;
import com.homekey.core.command.DimDeviceCommand;
import com.homekey.core.command.SwitchDeviceCommand;
import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.device.mock.MockDeviceSwitcher;
import com.homekey.core.http.HttpApi;
import com.homekey.core.main.InternalData;

public class InternalDataTest {
	private MockDeviceSwitcher dev1;
	private MockDeviceDimmer dev2;
	
	@Before
	public void setUp() throws Exception {
		dev1 = new MockDeviceSwitcher("DA", true);
		dev2 = new MockDeviceDimmer("DDD", true);
		dev1.setId(10);
		dev1.setName("My MockDevice #1");
		dev2.setId(20);
		dev2.setName("My MockDevice #2");
	}
	
	@After
	public void tearDown() throws Exception {}
	
	@Test
	public void testGetDevice() {
		InternalData m = new InternalData();
		
		// getDevice should return null for all getDevice
		assertEquals(m.getDevice(dev1.getId()), null);
		assertEquals(m.getDevice(dev2.getId()), null);
		assertEquals(m.getDevice(12345), null);
		
		m.addDevice(dev1);
		m.addDevice(dev2);
		
		// Return correct device
		assertEquals(m.getDevice(dev1.getId()), dev1);
		assertEquals(m.getDevice(dev2.getId()), dev2);
		assertNotSame(m.getDevice(dev2.getId()), dev1);
		assertNotSame(m.getDevice(dev1.getId()), dev2);
		
	}
	
	@Test
	public void testTurnOnOff() {
		InternalData m = new InternalData();
		CommandQueue q = new CommandQueue();
		CommandThread ct = new CommandThread(m, q);
		
		ct.start();
		
		m.addDevice(dev1);
		m.addDevice(dev2);
		
		for (int i = 0; i < 10; i++) {
			assertTrue(new SwitchDeviceCommand(dev1.getId(), true).postAndWaitForResult(q));
			assertTrue(dev1.getValue());
			assertTrue(new SwitchDeviceCommand(dev1.getId(), false).postAndWaitForResult(q));
			assertTrue(!dev1.getValue());
			assertTrue(new SwitchDeviceCommand(dev2.getId(), true).postAndWaitForResult(q));
			assertTrue("SwitchDevice should be 255 when on, but it is " + dev2.getValue(), dev2.getValue() == 255);
			assertTrue(new SwitchDeviceCommand(dev2.getId(), false).postAndWaitForResult(q));
			assertTrue("SwitchDevice should be 0 when off, but it is " + dev2.getValue(), dev2.getValue() == 0);
		}
		
		ct.interrupt();
	}
	
	@Test
	public void testGetDevices() {
		InternalData m = new InternalData();
		CommandQueue q = new CommandQueue();
		CommandThread qt = new CommandThread(m, q);
		HttpApi api = new HttpApi(q);
		
		qt.start();
		
		m.addDevice(dev1);
		m.addDevice(dev2);
		
		String s = api.getDevices();
		
		System.out.println("HTTAPI: " + api.getDevices());
		
		assertTrue("s is " + s,s.contains("My MockDevice #1") && s.contains("MockDeviceSwitcher"));
		assertTrue("s is " + s,s.contains("My MockDevice #2") && s.contains("MockDeviceDimmer"));
		
		qt.interrupt();
	}
	
	@Test
	public void testTakeCommandPreservesOrder() {
		CommandQueue q = new CommandQueue();
		
		for (int i = 0; i < 10000; i++) {
			// kind of random level
			int level = (i * 199 + i * i + 200) % 255;
			DimDeviceCommand cmd1 = new DimDeviceCommand(dev2.getId(), level);
			q.post(cmd1);
			boolean turnOn = (i & 1) == 1;
			SwitchDeviceCommand cmd2 = new SwitchDeviceCommand(dev1.getId(), turnOn);
			q.post(cmd2);
		}
		
		for (int i = 0; i < 1000; i++) {
			// kind of random level
			int level = (i * 199 + i * i + 200) % 255;
			try {
				assertTrue(((DimDeviceCommand) q.takeCommand()).getLevel() == level);
			} catch (InterruptedException e) {}
			boolean turnOn = (i & 1) == 1;
			try {
				assertTrue(((SwitchDeviceCommand) q.takeCommand()).turningOn() == turnOn);
			} catch (InterruptedException e) {}
		}
		
	}
}
