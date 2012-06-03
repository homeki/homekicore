package com.homeki.core.http.handlers;

import static org.testng.Assert.assertEquals;

import java.util.Date;

import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

public class DeviceTellStickHandlerTest {
	public class JsonTellStickDevice {
		public String type;
		public Integer id;
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
		assertEquals(200, TestUtil.sendPost("/device/tellstick/add", dev).statusCode);
		
		dev = new JsonTellStickDevice();
		dev.name = "tellstick dimmer 1";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		assertEquals(200, TestUtil.sendPost("/device/tellstick/add", dev).statusCode);
		
		dev = new JsonTellStickDevice();
		dev.name = "tellstick dimmer 2";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.house = 12000;
		dev.unit = 5;
		assertEquals(200, TestUtil.sendPost("/device/tellstick/add", dev).statusCode);
	}
}
