package com.homekey.core.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.DeviceInformation;
import com.homekey.core.device.onewire.OneWireDetector;
import com.homekey.core.device.onewire.OneWireTemperatureDevice;

public class OneWireDetectorTest {
	private static final String ONEWIRE_ROOT_DIR = "test/1wire/uncached";
	
	private OneWireDetector owd;
	
	@Before
	public void setUp() throws Exception {
		owd = new OneWireDetector(ONEWIRE_ROOT_DIR);
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testFindDevices() {
		List<DeviceInformation> devices = owd.findDevices();
		
		DeviceInformation a = devices.get(0);
		assertEquals("10.7B3F7F010800", a.getInternalId());
		assertEquals(OneWireTemperatureDevice.class, a.getType());
		assertEquals(ONEWIRE_ROOT_DIR + "/" + a.getInternalId(), a.getAdditionalData("deviceDirPath"));
	}
}
