package com.homeki.core.http.handlers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;

public class DeviceStateHandlerTest {
	private int id1;
	private int id2;
	
	public class JsonState {
		public Object value;
		public Integer level;
	}
	
	@BeforeClass
	private void beforeClass() {
		id1 = TestUtil.addMockDevice(MockDeviceType.SWITCH);
		id2 = TestUtil.addMockDevice(MockDeviceType.DIMMER);
	}
	
	@AfterClass
	private void afterClass() {
		TestUtil.deleteDevice(id1);
		TestUtil.deleteDevice(id2);
	}
	
	@Test
	public void testSet() {
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id1 + "&channel=5&value=0").statusCode, 405);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id1 + "&channel=0&value=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id1 + "&channel=0&value=0").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id1 + "&channel=0&value=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id1 + "&channel=0&value=0").statusCode, 200);
		
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id2 + "&channel=0&value=0").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id2 + "&channel=0&value=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id2 + "&channel=1&value=100").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id2 + "&channel=1&value=200").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id2 + "&channel=1&value=220").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/state/set?deviceid=" + id2 + "&channel=0&value=0").statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSet")
	public void testList() {
		JsonState[] states = TestUtil.sendGetAndParseAsJson("/device/state/list?from=2000-01-01&to=2100-01-01&deviceid=" + id1, JsonState[].class);
		assertTrue(states.length >= 5);
		
		states = TestUtil.sendGetAndParseAsJson("/device/state/list?from=2000-01-01&to=2100-01-01&deviceid=" + id2, JsonState[].class);
		assertTrue(states.length >= 4);
		
		states = TestUtil.sendGetAndParseAsJson("/device/state/list?from=2000-01-01&to=2100-01-01&deviceid=" + id2 + "&channel=0", JsonState[].class);
		assertTrue(states.length >= 4);
		
		states = TestUtil.sendGetAndParseAsJson("/device/state/list?from=2000-01-01&to=2100-01-01&deviceid=" + id2 + "&channel=1", JsonState[].class);
		assertTrue(states.length >= 4);
	}
}
