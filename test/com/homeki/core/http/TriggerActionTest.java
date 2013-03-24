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

public class TriggerActionTest {
	private int triggerId;
	private int actionId1;
	private int actionId2;
	private int actionGroupId;
	private int deviceId1;
	private int deviceId2;
	
	public class JsonTrigger {
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
	
	public class JsonTriggerActionGroupAction extends JsonAction {
		public Integer actionGroupId;
	}
	
	public class JsonActionGroup {
		public Integer id;
		public String name;
	}
	
	@BeforeClass
	public void beforeClass() {
		JsonTrigger jtrigger = new JsonTrigger();
		jtrigger.name = "foractiontest";
		jtrigger = TestUtil.sendPostAndParseAsJson("/trigger/add", jtrigger, JsonTrigger.class);
		triggerId = jtrigger.id;
		
		JsonActionGroup jactgrp = new JsonActionGroup();
		jactgrp.name = "Test action group";
		jactgrp = TestUtil.sendPostAndParseAsJson("/actiongroup/add", jactgrp, JsonActionGroup.class);
		actionGroupId = jactgrp.id;
		
		deviceId1 = TestUtil.addMockDevice(MockDeviceType.SWITCH);
		deviceId2 = TestUtil.addMockDevice(MockDeviceType.DIMMER);
	}
	
	@AfterClass
	public void afterClass() {
		TestUtil.deleteDevice(deviceId1);
		TestUtil.deleteDevice(deviceId2);
		TestUtil.sendGet("/trigger/" + triggerId + "/delete");
		TestUtil.sendGet("/actiongroup/" + actionGroupId + "/delete");
	}
	
	@Test
	public void testAddChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.deviceId = deviceId1;
		jact.value = 1;
		jact.channel = 1;
		
		assertEquals(TestUtil.sendPost("/trigger/9999/action/add?type=changechannelvalue", jact).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/action/add?type=feelchangechannelvalue", jact).statusCode, 400);
		
		jact = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/action/add?type=changechannelvalue", jact, JsonChangeChannelValueAction.class);
		
		assertTrue(jact.id > 0);
		actionId1 = jact.id;
	}
	
	@Test(dependsOnMethods="testAddChangeChannelValueAction")
	public void testAddTriggerActionGroupAction() {
		JsonTriggerActionGroupAction jact = new JsonTriggerActionGroupAction();
		jact.actionGroupId = 9238298;
		
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/add?type=triggeractiongroup", jact).statusCode, 400);
		
		jact.actionGroupId = actionGroupId;
		jact = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/action/add?type=triggeractiongroup", jact, JsonTriggerActionGroupAction.class);
		
		assertTrue(jact.id > 0);
		actionId2 = jact.id;
	}
	
	@Test(dependsOnMethods="testAddTriggerActionGroupAction")
	public void testList() {
		JsonAction[] jactions = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/action/list", JsonAction[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonAction jc : jactions)
			existingIds.add(jc.id);
		
		assertTrue(existingIds.contains(actionId1));
		assertTrue(existingIds.contains(actionId2));
	}
	
	@Test(dependsOnMethods="testList")
	public void testSetChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.deviceId = deviceId2;
		jact.value = 3;
		jact.channel = 2;
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/" + actionId1 + "/set", jact).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetChangeChannelValueAction")
	public void testGetChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/action/" + actionId1 + "/get", JsonChangeChannelValueAction.class);
		assertEquals((int)jact.channel, 2);
		assertEquals(jact.value.intValue(), 3);
		assertEquals((int)jact.deviceId, deviceId2);
	}
	
	@Test(dependsOnMethods="testGetChangeChannelValueAction")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/delete").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/get").statusCode, 400);
	}
}
