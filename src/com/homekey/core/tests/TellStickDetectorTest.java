package com.homekey.core.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.DeviceInformation;
import com.homekey.core.device.tellstick.TellStickDetector;
import com.homekey.core.device.tellstick.TellStickDimmer;
import com.homekey.core.device.tellstick.TellStickSwitch;
import com.homekey.core.storage.Database;
import com.homekey.core.storage.mock.MockDatabase;

public class TellStickDetectorTest {
	private static final String TEST_CONF_PATH = "src/com/homekey/core/tests/files/tellstick.conf";
	
	private TellStickDetector tsd;
	
	@Before
	public void setUp() throws Exception {
		tsd = new TellStickDetector(TEST_CONF_PATH);
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testParsing() {
		List<DeviceInformation> devices = tsd.findDevices();

		DeviceInformation a = devices.get(0);
		DeviceInformation b = devices.get(1);
		
		//assertTrue(a.getInternalId().equals("1"));
		//assertTrue(b.getInternalId().equals("2"));
		assertTrue(a.getType() == TellStickSwitch.class);
		assertTrue(b.getType() ==  TellStickDimmer.class);		
	}
	
}
