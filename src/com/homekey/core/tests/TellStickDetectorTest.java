package com.homekey.core.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.homekey.core.device.Device;
import com.homekey.core.device.tellstick.TellStickDetector;
import com.homekey.core.device.tellstick.TellStickDimmer;
import com.homekey.core.device.tellstick.TellStickSwitch;

public class TellStickDetectorTest {
	private static final String path = "src/com/homekey/core/tests/files/tellstick.conf";
	private TellStickDetector tsd;
	
	@Before
	public void setUp() throws Exception {
		tsd = new TellStickDetector(path);
	}
	
	@After
	public void tearDown() throws Exception {}
	
	@Test
	public void testParsing() {
		java.util.List<Device> devices = tsd.findDevices();

		Device a = devices.get(0);
		Device b = devices.get(1);
		
		assertTrue(a.getInternalId().equals("1"));
		assertTrue(b.getInternalId().equals("2"));
		assertTrue(a instanceof TellStickSwitch);
		assertTrue(b instanceof TellStickDimmer);		
	}
	
}
