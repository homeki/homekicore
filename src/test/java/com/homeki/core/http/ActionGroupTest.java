package com.homeki.core.http;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;
import com.homeki.core.main.Util;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ActionGroupTest {
	private int deviceId1;
	private int deviceId2;
	private int actionGroupId;
	
	public static class JsonActionGroup {
		public Integer actionGroupId;
		public String name;
	}
	
	public static class JsonAction {
		public String type;
		public Integer actionId;
	}
	
	public static class JsonChangeChannelValueAction extends JsonAction {
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
		assertEquals(TestUtil.sendPost("/actiongroups", jactgrp).statusCode, 400);
		jactgrp.name = "";
		assertEquals(TestUtil.sendPost("/actiongroups", jactgrp).statusCode, 400);
		jactgrp.name = "MyActionGroup";
		jactgrp = TestUtil.sendPostAndParseAsJson("/actiongroups", jactgrp, JsonActionGroup.class);
		assertTrue(jactgrp.actionGroupId > 0);
		actionGroupId = jactgrp.actionGroupId;
	}
	
	@Test(dependsOnMethods="testAdd")
	public void testAddChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.deviceId = deviceId2;
		jact.value = 1;
		jact.channel = 1;

		jact.type = "feeelchangechannelvalue";
		assertEquals(TestUtil.sendPost("/actiongroups/9999/actions", jact).statusCode, 400);
		assertEquals(TestUtil.sendPost("/actiongroups/" +  actionGroupId + "/actions", jact).statusCode, 400);

		jact.type = "changechannelvalue";
		jact = TestUtil.sendPostAndParseAsJson("/actiongroups/" + actionGroupId + "/actions", jact, JsonChangeChannelValueAction.class);
		
		assertTrue(jact.actionId > 0);
	}
	
	@Test(dependsOnMethods="testAddChangeChannelValueAction")
	public void testTrigger() {
		assertEquals(TestUtil.sendGet("/actiongroups/" + actionGroupId + "/trigger").statusCode, 200);
		Util.sleep(2000); // wait for the trigger to complete
	}
	
	@Test(dependsOnMethods="testTrigger")
	public void testList() {
		JsonActionGroup[] jactgrps = TestUtil.sendGetAndParseAsJson("/actiongroups", JsonActionGroup[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonActionGroup jd : jactgrps)
			existingIds.add(jd.actionGroupId);
		
		assertTrue(existingIds.contains(actionGroupId));
	}
	
	@Test(dependsOnMethods="testList")
	public void testSet() {
		JsonActionGroup jactgrp = new JsonActionGroup();
		assertEquals(TestUtil.sendPost("/actiongroups/9999/set", jactgrp).statusCode, 400);
		assertEquals(TestUtil.sendPost("/actiongroups/" + actionGroupId, jactgrp).statusCode, 400);
		jactgrp.name = "MyNewActionGroup";
		assertEquals(TestUtil.sendPost("/actiongroups/" + actionGroupId, jactgrp).statusCode, 200);
		
		JsonActionGroup[] jactgrps = TestUtil.sendGetAndParseAsJson("/actiongroups", JsonActionGroup[].class);
		
		boolean found = false;
		for (JsonActionGroup jt : jactgrps) {
			if (jt.actionGroupId == actionGroupId) {
				found = true;
				assertEquals(jt.name, "MyNewActionGroup");
			}
		}
		assertTrue(found);
	}
	
	@Test(dependsOnMethods="testSet")
	public void testDelete() {
		assertEquals(TestUtil.sendDelete("/actiongroups/" + actionGroupId).statusCode, 200);
	}
}
