package com.homeki.core.http;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;

public class TriggerActionGroupTest {
	private int deviceId1;
	private int deviceId2;
	private int actionGroupId;
	
	public class JsonActionGroup {
		public Integer id;
		public String name;
	}
	
	public class JsonAction {
		public String type;
		public Integer id;
	}
	
	public class JsonChangeChannelValueAction extends JsonAction {
		public Integer deviceId;
		public Integer channel;
		public Number value;
	}
	
	@BeforeClass
	public void beforeClass() {
		deviceId1 = TestUtil.addMockDevice(MockDeviceType.SWITCH);
		deviceId2 = TestUtil.addMockDevice(MockDeviceType.DIMMER);
	}
	
	@AfterClass
	public void afterClass() {
		TestUtil.deleteDevice(deviceId1);
		TestUtil.deleteDevice(deviceId2);
	}
	
	@Test
	public void testAdd() throws Exception {
		JsonActionGroup jactgrp = new JsonActionGroup();
		assertEquals(TestUtil.sendPost("/actiongroup/add", jactgrp).statusCode, 400);
		jactgrp.name = "";
		assertEquals(TestUtil.sendPost("/actiongroup/add", jactgrp).statusCode, 400);
		jactgrp.name = "MyActionGroup";
		jactgrp = TestUtil.sendPostAndParseAsJson("/actiongroup/add", jactgrp, JsonActionGroup.class);
		assertTrue(jactgrp.id > 0);
		actionGroupId = jactgrp.id;
	}
	
	@Test(dependsOnMethods="testAdd")
	public void testAddChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.deviceId = deviceId1;
		jact.value = 1;
		jact.channel = 1;
		
		assertEquals(TestUtil.sendPost("/actiongroup/9999/action/add?type=changechannelvalue", jact).statusCode, 400);
		assertEquals(TestUtil.sendPost("/actiongroup/" +  actionGroupId + "/action/add?type=feelchangechannelvalue", jact).statusCode, 400);
		
		jact = TestUtil.sendPostAndParseAsJson("/actiongroup/" + actionGroupId + "/action/add?type=changechannelvalue", jact, JsonChangeChannelValueAction.class);
		
		assertTrue(jact.id > 0);
	}
	
	@Test(dependsOnMethods="testAddChangeChannelValueAction")
	public void testList() {
		JsonActionGroup[] jactgrps = TestUtil.sendGetAndParseAsJson("/actiongroup/list", JsonActionGroup[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonActionGroup jd : jactgrps)
			existingIds.add(jd.id);
		
		assertTrue(existingIds.contains(actionGroupId));
	}
	
	@Test(dependsOnMethods="testList")
	public void testSet() {
		JsonActionGroup jactgrp = new JsonActionGroup();
		assertEquals(TestUtil.sendPost("/actiongroup/9999/set", jactgrp).statusCode, 400);
		assertEquals(TestUtil.sendPost("/actiongroup/" + actionGroupId + "/set", jactgrp).statusCode, 400);
		jactgrp.name = "MyNewActionGroup";
		assertEquals(TestUtil.sendPost("/actiongroup/" + actionGroupId + "/set", jactgrp).statusCode, 200);
		
		JsonActionGroup[] jactgrps = TestUtil.sendGetAndParseAsJson("/actiongroup/list", JsonActionGroup[].class);
		
		boolean found = false;
		for (JsonActionGroup jt : jactgrps) {
			if (jt.id == actionGroupId) {
				found = true;
				assertEquals(jt.name, "MyNewActionGroup");
			}
		}
		assertTrue(found);
	}
	
	@Test(dependsOnMethods="testSet")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/actiongroup/" + actionGroupId + "/delete").statusCode, 200);
	}
}
