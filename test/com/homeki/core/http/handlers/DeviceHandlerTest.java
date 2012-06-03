package com.homeki.core.http.handlers;

import static org.testng.Assert.assertEquals;

import java.util.Date;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class DeviceHandlerTest {
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
	
	@Test
	public void testList() {
		JsonDevice[] jdevices = TestUtil.sendGetAndParseAsJson("/device/list", JsonDevice[].class);
		
		assertEquals((int)jdevices[0].id, 1);
		assertEquals((int)jdevices[1].id, 2);
		assertEquals((int)jdevices[2].id, 3);
		assertEquals((int)jdevices[3].id, 4);
		assertEquals((int)jdevices[4].id, 5);
		
		assertEquals(jdevices[0].type, "switch");
		assertEquals(jdevices[1].type, "switch");
		assertEquals(jdevices[2].type, "dimmer");
		assertEquals(jdevices[3].type, "thermometer");
		assertEquals(jdevices[4].type, "thermometer");
	}
	
	@Test
	public void testGet() {
		assertEquals(TestUtil.sendGet("/device/get?deviceid=999999").statusCode, 405);
		JsonDevice dev = TestUtil.sendGetAndParseAsJson("/device/get?deviceid=1", JsonDevice.class);
		assertEquals((int)dev.id, 1);
	}
	
	@Test
	public void testSet() {
		JsonDevice beforeDev = TestUtil.sendGetAndParseAsJson("/device/get?deviceid=2", JsonDevice.class);
		assertEquals(beforeDev.name, "");
		assertEquals(beforeDev.description, "");
		
		JsonDevice dev = new JsonDevice();
		dev.name = "living room corner";	
		dev.description = "corner lamps in living room";
		assertEquals(TestUtil.sendPost("/device/set?deviceid=2", dev).statusCode, 200);
		
		JsonDevice afterDev = TestUtil.sendGetAndParseAsJson("/device/get?deviceid=2", JsonDevice.class);
		assertEquals(afterDev.name, "living room corner");
		assertEquals(afterDev.description, "corner lamps in living room");
	}
	
	@Test(dependsOnMethods= { "testGet", "testSet" })
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/device/delete?deviceid=99999").statusCode, 405);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/delete?deviceid=1").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=1").statusCode, 405);
	}
	
	@Test(dependsOnMethods="testDelete")
	public void testMerge() {
		assertEquals(TestUtil.sendGet("/device/get?deviceid=4").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=5").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/merge?sourcedeviceid=5&targetdeviceid=4").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=4").statusCode, 200);
		assertEquals(TestUtil.sendGet("/device/get?deviceid=5").statusCode, 405);
	}
}
