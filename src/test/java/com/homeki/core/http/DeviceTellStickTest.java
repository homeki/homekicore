package com.homeki.core.http;

import com.homeki.core.TestUtil;
import org.testng.annotations.Test;

import java.util.Date;

public class DeviceTellStickTest {
	private int id1;
	private int id2;
	private int id3;

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
		JsonTellStickDevice jid1 = TestUtil.sendPostAndParseAsJson("/devices", dev, JsonTellStickDevice.class);
		id1 = jid1.deviceId;
		
		dev = new JsonTellStickDevice();
		dev.vendor = "tellstick";
		dev.name = "tellstick dimmer 1";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.protocol = "arctech";
		dev.model = "selflearning-dimmer";
		dev.house = "12002";
		dev.unit = "7";
		JsonTellStickDevice jid2 = TestUtil.sendPostAndParseAsJson("/devices", dev, JsonTellStickDevice.class);
		id2 = jid2.deviceId;
		
		dev = new JsonTellStickDevice();
		dev.vendor = "tellstick";
		dev.name = "tellstick dimmer 2";
		dev.description = "dimmer description";
		dev.type = "dimmer";
		dev.protocol = "arctech";
		dev.model = "selflearning-dimmer";
		dev.house = "12000";
		dev.unit = "5";
		JsonTellStickDevice jid3 = TestUtil.sendPostAndParseAsJson("/devices", dev, JsonTellStickDevice.class);
		id3 = jid3.deviceId;
	}

	@Test(dependsOnMethods = "testAdd")
	public void testDelete() throws Exception {
		TestUtil.deleteDevice(id1);
		TestUtil.deleteDevice(id2);
		TestUtil.deleteDevice(id3);
	}
}
