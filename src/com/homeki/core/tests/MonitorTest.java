package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockSwitch;
import com.homeki.core.device.mock.MockThermometer;
import com.homeki.core.main.Monitor;
import com.homeki.core.storage.ITableFactory;

public class MonitorTest {
	@Test
	public void testGetLoggableDevices() {
		Monitor m = new Monitor();
		ITableFactory db = TestUtil.getEmptySqliteTestTableFactory();
		assertEquals(0, m.getLoggableDevices().size());
		m.addDevice(new MockSwitch("asdf", db));
		assertEquals(0, m.getLoggableDevices().size());
		m.addDevice(new MockDimmer("asdf", db));
		assertEquals(0, m.getLoggableDevices().size());
		m.addDevice(new MockThermometer("mylogdev1", db));
		assertEquals(1, m.getLoggableDevices().size());
		m.addDevice(new MockThermometer("mylogdev2", db));
		assertEquals(2, m.getLoggableDevices().size());
		m.addDevice(new MockDimmer("asdf", db));
		assertEquals(2, m.getLoggableDevices().size());
	}

	@Test
	public void testContainsDevice(){
		Monitor m = new Monitor();
		ITableFactory db = TestUtil.getEmptySqliteTestTableFactory();
		m.addDevice(new MockSwitch("switch1", db));
		MockSwitch switch2 = new MockSwitch("switch2", db);
		assertFalse("Device was found before it was added.",m.containsDevice("switch2"));
		m.addDevice(switch2);
		assertTrue("Device was not found after it was added.",m.containsDevice("switch2"));
	}
}
