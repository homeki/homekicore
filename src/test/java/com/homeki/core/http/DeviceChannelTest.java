package com.homeki.core.http;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DeviceChannelTest {
	private int id1;
	private int id2;
	
	public static class JsonState {
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
		assertEquals(TestUtil.sendGet("/devices/" + id1 + "/channel/5/set?value=0").statusCode, 400);
		assertEquals(TestUtil.sendGet("/devices/" + id1 + "/channel/0/set?value=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id1 + "/channel/0/set?value=0").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id1 + "/channel/0/set?value=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id1 + "/channel/0/set?value=0").statusCode, 200);
		
		assertEquals(TestUtil.sendGet("/devices/" + id2 + "/channel/0/set?value=0").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id2 + "/channel/0/set?value=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id2 + "/channel/1/set?value=100").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id2 + "/channel/1/set?value=200").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id2 + "/channel/1/set?value=220").statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + id2 + "/channel/0/set?value=0").statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSet")
	public void testList() {
		JsonState[] states = TestUtil.sendGetAndParseAsJson("/devices/" + id1 + "/channel/0/list?from=2000-01-01&to=2100-01-01", JsonState[].class);
		assertTrue(states.length >= 5);
		
		states = TestUtil.sendGetAndParseAsJson("/devices/" + id2 + "/channel/0/list?from=2000-01-01&to=2100-01-01", JsonState[].class);
		assertTrue(states.length >= 4);
		
		states = TestUtil.sendGetAndParseAsJson("/devices/" + id2 + "/channel/1/list?from=2000-01-01&to=2100-01-01", JsonState[].class);
		assertTrue(states.length >= 4);
	}
}
