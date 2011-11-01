package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.onewire.OneWireDetector;
import com.homeki.core.device.onewire.OneWireThermometer;

public class OneWireDetectorTest {
	private static final String ONEWIRE_ROOT_DIR = "test/1wire/uncached";
	
	private OneWireDetector owd;
	
	@Before
	public void setUp() throws Exception {
		owd = new OneWireDetector(ONEWIRE_ROOT_DIR);
	}
	
	@Test
	public void testFindDevices() {
		List<DeviceInformation> devices = owd.findDevices();
		
		DeviceInformation a = devices.get(0);
		assertEquals("10.7B3F7F010800", a.getInternalId());
		assertEquals(OneWireThermometer.class, a.getType());
		assertEquals(ONEWIRE_ROOT_DIR + "/" + a.getInternalId(), a.getAdditionalData("deviceDirPath"));
	}
}
