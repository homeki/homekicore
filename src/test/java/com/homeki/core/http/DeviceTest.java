package com.homeki.core.http;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class DeviceTest {
	private int[] ids;
	
	public static class JsonDevice {
		public String type;
		public Integer deviceId;
		public String name;
		public String description;
		public Date added;
		public Boolean active;
		public JsonState state;
	}
	
	public class JsonState {
		public Object value;
		public Integer level;
	}
	
	@BeforeClass
	private void beforeClass() {
		ids = new int[5];
		ids[0] = TestUtil.addMockDevice(MockDeviceType.SWITCH);
		ids[1] = TestUtil.addMockDevice(MockDeviceType.SWITCH);
		ids[2] = TestUtil.addMockDevice(MockDeviceType.DIMMER);
		ids[3] = TestUtil.addMockDevice(MockDeviceType.THERMOMETER);
		ids[4] = TestUtil.addMockDevice(MockDeviceType.THERMOMETER);
	}
	
	@AfterClass
	private void afterClass() {
		TestUtil.deleteDevice(ids[1]);
		TestUtil.deleteDevice(ids[2]);
		TestUtil.deleteDevice(ids[3]);
	}
	
	@Test
	public void testList() {
		JsonDevice[] jdevices = TestUtil.sendGetAndParseAsJson("/devices", JsonDevice[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonDevice jd : jdevices)
			existingIds.add(jd.deviceId);
			
		for (int i : ids)
			assertTrue(existingIds.contains(i));
	}
	
	@Test(dependsOnMethods="testList")
	public void testGet() {
		assertEquals(TestUtil.sendGet("/devices/999999").statusCode, 400);
		JsonDevice dev = TestUtil.sendGetAndParseAsJson("/devices/" + ids[0], JsonDevice.class);
		assertEquals((int)dev.deviceId, ids[0]);
	}
	
	@Test(dependsOnMethods="testGet")
	public void testSet() {
		JsonDevice beforeDev = TestUtil.sendGetAndParseAsJson("/devices/" + ids[1], JsonDevice.class);
		assertEquals(beforeDev.description, "");
		
		JsonDevice dev = new JsonDevice();
		dev.name = "living room corner";	
		dev.description = "corner lamps in living room";
		assertEquals(TestUtil.sendPost("/devices/" + ids[1], dev).statusCode, 200);
		
		JsonDevice afterDev = TestUtil.sendGetAndParseAsJson("/devices/" + ids[1], JsonDevice.class);
		assertEquals(afterDev.name, "living room corner");
		assertEquals(afterDev.description, "corner lamps in living room");
	}
	
	@Test(dependsOnMethods="testSet")
	public void testDelete() {
		assertEquals(TestUtil.sendDelete("/devices/99999").statusCode, 400);
		assertEquals(TestUtil.sendGet("/devices/" + ids[0]).statusCode, 200);
		assertEquals(TestUtil.sendDelete("/devices/" + ids[0]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + ids[0]).statusCode, 400);
	}
	
	@Test(dependsOnMethods="testDelete")
	public void testMerge() {
		assertEquals(TestUtil.sendGet("/devices/" + ids[3]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + ids[4]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + ids[3] + "/merge?mergewith=" + ids[4]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + ids[3]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/devices/" + ids[4]).statusCode, 400);
	}
}
