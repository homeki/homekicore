package com.homeki.core.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;

public class DeviceTest {
	private int[] ids;
	
	public class JsonDevice {
		public String type;
		public Integer id;
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
		TestUtil.deleteDevice(ids[4]);
	}
	
	@Test
	public void testList() {
		JsonDevice[] jdevices = TestUtil.sendGetAndParseAsJson("/device/list", JsonDevice[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonDevice jd : jdevices)
			existingIds.add(jd.id);
			
		for (int i : ids)
			assertTrue(existingIds.contains(i));
	}
	
	@Test(dependsOnMethods="testList")
	public void testGet() {
		assertEquals(TestUtil.sendGet("/device/999999/get").statusCode, 400);
		JsonDevice dev = TestUtil.sendGetAndParseAsJson("/device/" + ids[0] + "/get", JsonDevice.class);
		assertEquals((int)dev.id, ids[0]);
	}
	
	@Test(dependsOnMethods="testGet")
	public void testSet() {
		JsonDevice beforeDev = TestUtil.sendGetAndParseAsJson("/device/" + ids[1] + "/get", JsonDevice.class);
		assertEquals(beforeDev.name, "");
		assertEquals(beforeDev.description, "");
		
		JsonDevice dev = new JsonDevice();
		dev.name = "living room corner";	
		dev.description = "corner lamps in living room";
		assertEquals(TestUtil.sendPost("/device/" + ids[1] + "/set", dev).statusCode, 200);
		
		JsonDevice afterDev = TestUtil.sendGetAndParseAsJson("/device/" + ids[1] + "/get", JsonDevice.class);
		assertEquals(afterDev.name, "living room corner");
		assertEquals(afterDev.description, "corner lamps in living room");
	}
	
	@Test(dependsOnMethods="testSet")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/device/99999/delete").statusCode, 400);
		assertEquals(TestUtil.sendGet("/device/" + ids[0] + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/" + ids[0] + "/delete").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/" + ids[0] + "/get").statusCode, 400);
	}
	
	@Test(dependsOnMethods="testDelete")
	public void testMerge() {
		assertEquals(TestUtil.sendGet("/device/" + ids[3] + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/" + ids[4] + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/" + ids[3] + "/merge?mergewith=" + ids[4]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/" + ids[3] + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/" + ids[4] + "/get").statusCode, 400);
	}
}
