package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.mock.MockDeviceDimmer;
import com.homekey.core.device.mock.MockDeviceSwitcher;
import com.homekey.core.http.HttpApi;
import com.homekey.core.main.Monitor;

public class InternalDataTest {
	private MockDeviceSwitcher dev1;
	private MockDeviceDimmer dev2;
	
	@Before
	public void setUp() throws Exception {
		dev1 = new MockDeviceSwitcher("DA");
		dev2 = new MockDeviceDimmer("DDD");
		//dev1.setId(10);
		dev1.setName("My MockDevice #1");
		//dev2.setId(20);
		dev2.setName("My MockDevice #2");
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
//		Monitor m = new Monitor();
//		CommandQueue q = new CommandQueue();
//		CommandThread ct = new CommandThread(m, q);
//		
//		ct.start();
//		
//		m.addDevice(dev1);
//		m.addDevice(dev2);
//		
//		for (int i = 0; i < 10; i++) {
//			assertTrue(new SwitchDeviceCommand(dev1.getId(), true).postAndWaitForResult(q));
//			assertTrue(dev1.getValue());
//			assertTrue(new SwitchDeviceCommand(dev1.getId(), false).postAndWaitForResult(q));
//			assertTrue(!dev1.getValue());
//			assertTrue(new SwitchDeviceCommand(dev2.getId(), true).postAndWaitForResult(q));
//			assertTrue("SwitchDevice should be 255 when on, but it is " + dev2.getValue(), dev2.getValue() == 255);
//			assertTrue(new SwitchDeviceCommand(dev2.getId(), false).postAndWaitForResult(q));
//			assertTrue("SwitchDevice should be 0 when off, but it is " + dev2.getValue(), dev2.getValue() == 0);
//		}
//		
//		ct.interrupt();
	}
	
	@Test
	public void testGetDevices() {
		Monitor m = new Monitor();
		HttpApi api = new HttpApi(m);
		
		m.addDevice(dev1);
		m.addDevice(dev2);
		
		String s = api.getDevices();
		
		assertTrue("s is " + s,s.contains("My MockDevice #1") && s.contains("MockDeviceSwitcher"));
		assertTrue("s is " + s,s.contains("My MockDevice #2") && s.contains("MockDeviceDimmer"));
		
	}
	
}
