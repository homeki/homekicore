package com.homeki.core.http.handlers;

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

public class DeviceHandlerTest {
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
		assertEquals(TestUtil.sendGet("/device/get?deviceid=999999").statusCode, 405);
		JsonDevice dev = TestUtil.sendGetAndParseAsJson("/device/get?deviceid=" + ids[0], JsonDevice.class);
		assertEquals((int)dev.id, ids[0]);
	}
	
	@Test(dependsOnMethods="testGet")
	public void testSet() {
		JsonDevice beforeDev = TestUtil.sendGetAndParseAsJson("/device/get?deviceid=" + ids[1], JsonDevice.class);
		assertEquals(beforeDev.name, "");
		assertEquals(beforeDev.description, "");
		
		JsonDevice dev = new JsonDevice();
		dev.name = "living room corner";	
		dev.description = "corner lamps in living room";
		assertEquals(TestUtil.sendPost("/device/set?deviceid=" + ids[1], dev).statusCode, 200);
		
		JsonDevice afterDev = TestUtil.sendGetAndParseAsJson("/device/get?deviceid=" + ids[1], JsonDevice.class);
		assertEquals(afterDev.name, "living room corner");
		assertEquals(afterDev.description, "corner lamps in living room");
	}
	
	@Test(dependsOnMethods="testSet")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/device/delete?deviceid=99999").statusCode, 405);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=" + ids[0]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/delete?deviceid=" + ids[0]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=" + ids[0]).statusCode, 405);
	}
	
	@Test(dependsOnMethods="testDelete")
	public void testMerge() {
		assertEquals(TestUtil.sendGet("/device/get?deviceid=" + ids[3]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=" + ids[4]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/merge?sourcedeviceid=" + ids[4] + "&targetdeviceid=" + ids[3]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=" + ids[3]).statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=" + ids[4]).statusCode, 405);
	}
}
