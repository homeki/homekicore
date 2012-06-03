package com.homeki.core.http.handlers;

import java.util.Date;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class DeviceTellStickHandlerTest {
	public class JsonTellStickDevice {
		public Integer id;
		public String type;
		public String name;
		public String description;
		public Date added;
		public Integer house;
		public Integer unit;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTellStickDevice dev = new JsonTellStickDevice();
		dev.name = "tellstick switch 1";
		dev.description = "switch description";
		dev.type = "switch";
		JsonTellStickDevice id1 = TestUtil.sendPostAndParseAsJson("/device/tellstick/add", dev, JsonTellStickDevice.class);
		
		dev = new JsonTellStickDevice();
		dev.name = "tellstick dimmer 1";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		JsonTellStickDevice id2 = TestUtil.sendPostAndParseAsJson("/device/tellstick/add", dev, JsonTellStickDevice.class);
		
		dev = new JsonTellStickDevice();
		dev.name = "tellstick dimmer 2";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.house = 12000;
		dev.unit = 5;
		JsonTellStickDevice id3 = TestUtil.sendPostAndParseAsJson("/device/tellstick/add", dev, JsonTellStickDevice.class);
		
		TestUtil.sendGet("/device/delete?deviceid=" + id1.id);
		TestUtil.sendGet("/device/delete?deviceid=" + id2.id);
		TestUtil.sendGet("/device/delete?deviceid=" + id3.id);
	}
}
