package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.homekey.core.device.mock.MockDimmerDevice;
import com.homekey.core.device.mock.MockSwitchDevice;
import com.homekey.core.device.mock.MockTemperatureDevice;
import com.homekey.core.main.Monitor;
import com.homekey.core.storage.ITableFactory;

public class MonitorTest {
	@Test
	public void testGetLoggableDevices() {
		Monitor m = new Monitor();
		ITableFactory db = TestUtil.getEmptySqliteTestTableFactory();
		assertEquals(0, m.getLoggableDevices().size());
		m.addDevice(new MockSwitchDevice("asdf", db));
		assertEquals(0, m.getLoggableDevices().size());
		m.addDevice(new MockDimmerDevice("asdf", db));
		assertEquals(0, m.getLoggableDevices().size());
		m.addDevice(new MockTemperatureDevice("mylogdev1", db));
		assertEquals(1, m.getLoggableDevices().size());
		m.addDevice(new MockTemperatureDevice("mylogdev2", db));
		assertEquals(2, m.getLoggableDevices().size());
		m.addDevice(new MockDimmerDevice("asdf", db));
		assertEquals(2, m.getLoggableDevices().size());
	}

	@Test
	public void testContainsDevice(){
		Monitor m = new Monitor();
		ITableFactory db = TestUtil.getEmptySqliteTestTableFactory();
		m.addDevice(new MockSwitchDevice("switch1", db));
		MockSwitchDevice switch2 = new MockSwitchDevice("switch2", db);
		assertFalse("Device was found before it was added.",m.containsDevice("switch2"));
		m.addDevice(switch2);
		assertTrue("Device was not found after it was added.",m.containsDevice("switch2"));
	}
}
