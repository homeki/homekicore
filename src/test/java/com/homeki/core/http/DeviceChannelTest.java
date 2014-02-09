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

	public static class JsonChannelValue {
		public int value;

		public JsonChannelValue() {

		}

		public JsonChannelValue(int value) {
			this.value = value;
		}
	}

	public static class JsonHistoryPoint {
		public Object value;
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
		assertEquals(TestUtil.sendPost("/devices/" + id1 + "/channels/5", new JsonChannelValue(0)).statusCode, 400);
		assertEquals(TestUtil.sendPost("/devices/" + id1 + "/channels/0", new JsonChannelValue(1)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id1 + "/channels/0", new JsonChannelValue(0)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id1 + "/channels/0", new JsonChannelValue(1)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id1 + "/channels/0", new JsonChannelValue(0)).statusCode, 200);

		assertEquals(TestUtil.sendPost("/devices/" + id2 + "/channels/0", new JsonChannelValue(0)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id2 + "/channels/0", new JsonChannelValue(1)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id2 + "/channels/1", new JsonChannelValue(100)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id2 + "/channels/1", new JsonChannelValue(200)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id2 + "/channels/1", new JsonChannelValue(220)).statusCode, 200);
		assertEquals(TestUtil.sendPost("/devices/" + id2 + "/channels/0", new JsonChannelValue(0)).statusCode, 200);
	}

	@Test(dependsOnMethods = "testSet")
	public void testList() {
		JsonHistoryPoint[] states = TestUtil.sendGetAndParseAsJson("/devices/" + id1 + "/channels/0?from=2000-01-01&to=2100-01-01", JsonHistoryPoint[].class);
		assertTrue(states.length >= 5);

		states = TestUtil.sendGetAndParseAsJson("/devices/" + id2 + "/channels/0?from=2000-01-01&to=2100-01-01", JsonHistoryPoint[].class);
		assertTrue(states.length >= 4);

		states = TestUtil.sendGetAndParseAsJson("/devices/" + id2 + "/channels/1?from=2000-01-01&to=2100-01-01", JsonHistoryPoint[].class);
		assertTrue(states.length >= 4);
	}
}
