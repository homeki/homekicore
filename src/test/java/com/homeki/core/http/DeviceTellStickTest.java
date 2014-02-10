package com.homeki.core.http;

import com.homeki.core.TestUtil;
import org.testng.annotations.Test;

import java.util.Date;

public class DeviceTellStickTest {
	public static class JsonTellStickDevice {
		public Integer deviceId;
		public String vendor;
		public String type;
		public String name;
		public String description;
		public Date added;
		public String protocol;
		public String model;
		public String house;
		public String unit;
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonTellStickDevice dev = new JsonTellStickDevice();
		dev.vendor = "tellstick";
		dev.name = "tellstick switch 1";
		dev.description = "switch description";
		dev.type = "switch";
		dev.protocol = "arctech";
		dev.model = "selflearning-switch";
		dev.house = "12001";
		dev.unit = "6";
		JsonTellStickDevice id1 = TestUtil.sendPostAndParseAsJson("/devices", dev, JsonTellStickDevice.class);
		
		dev = new JsonTellStickDevice();
		dev.vendor = "tellstick";
		dev.name = "tellstick dimmer 1";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.protocol = "arctech";
		dev.model = "selflearning-dimmer";
		dev.house = "12002";
		dev.unit = "7";
		JsonTellStickDevice id2 = TestUtil.sendPostAndParseAsJson("/devices", dev, JsonTellStickDevice.class);
		
		dev = new JsonTellStickDevice();
		dev.vendor = "tellstick";
		dev.name = "tellstick dimmer 2";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.protocol = "arctech";
		dev.model = "selflearning-dimmer";
		dev.house = "12000";
		dev.unit = "5";
		JsonTellStickDevice id3 = TestUtil.sendPostAndParseAsJson("/devices", dev, JsonTellStickDevice.class);
		
		TestUtil.deleteDevice(id1.deviceId);
		TestUtil.deleteDevice(id2.deviceId);
		TestUtil.deleteDevice(id3.deviceId);
	}
}
