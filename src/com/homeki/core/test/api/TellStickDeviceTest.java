package com.homeki.core.test.api;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class TellStickDeviceTest {
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
	public void testAdd() {
		JsonTellStickDevice dev = new JsonTellStickDevice();
		dev.name = "tellstick switch 1";
		dev.description = "switch description";
		dev.type = "switch";
		assertEquals(200, TestUtils.sendPost("/device/tellstick/add", dev));
		
		dev = new JsonTellStickDevice();
		dev.name = "tellstick dimmer 1";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		assertEquals(200, TestUtils.sendPost("/device/tellstick/add", dev));
		
		dev = new JsonTellStickDevice();
		dev.name = "tellstick dimmer 2";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.house = 12000;
		dev.unit = 5;
		assertEquals(200, TestUtils.sendPost("/device/tellstick/add", dev));
	}
}
