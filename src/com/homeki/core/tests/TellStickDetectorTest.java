package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.device.DeviceInformation;
import com.homeki.core.device.tellstick.TellStickDetector;
import com.homeki.core.device.tellstick.TellStickDimmer;
import com.homeki.core.device.tellstick.TellStickSwitch;

public class TellStickDetectorTest {
	private static final String TEST_CONF_PATH = "test/tellstick/tellstick.conf";
	
	private TellStickDetector tsd;
	
	@Before
	public void setUp() throws Exception {
		tsd = new TellStickDetector(TEST_CONF_PATH);
	}
	
	@Test
	public void testFindDevices() {
		List<DeviceInformation> devices = tsd.findDevices();

		DeviceInformation a = devices.get(0);
		assertEquals("1", a.getInternalId());
		assertEquals(TellStickSwitch.class, a.getType());
		
		DeviceInformation b = devices.get(1);
		assertEquals("2", b.getInternalId());
		assertEquals(TellStickDimmer.class, b.getType());		
	}
}
