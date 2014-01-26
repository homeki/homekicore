package com.homeki.core.http;

import com.homeki.core.TestUtil;
import com.homeki.core.TestUtil.MockDeviceType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TriggerActionTest {
	private int triggerId;
	private int actionId1;
	private int actionId2;
	private int actionId3;
	private int actionGroupId;
	private int deviceId1;
	private int deviceId2;
	
	public static class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	public static class JsonAction {
		public String type;
		public Integer id;
	}
	
	public static class JsonChangeChannelValueAction extends JsonAction {
		public Integer deviceId;
		public Integer channel;
		public Number value;
	}
	
	public static class JsonTriggerActionGroupAction extends JsonAction {
		public Integer actionGroupId;
	}
	
	public static class JsonSendMailAction extends JsonAction {
		public String subject;
		public String recipients;
		public String text;
	}
	
	public static class JsonActionGroup {
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

		jact.type = "changechannelvalue";
		assertEquals(TestUtil.sendPost("/trigger/9999/action/add", jact).statusCode, 400);

		jact.type = "feeelchangechannelvalue";
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/action/add", jact).statusCode, 400);

		jact.type = "changechannelvalue";
		jact = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/action/add", jact, JsonChangeChannelValueAction.class);
		
		assertTrue(jact.id > 0);
		actionId1 = jact.id;
	}
	
	@Test(dependsOnMethods="testAddChangeChannelValueAction")
	public void testAddTriggerActionGroupAction() {
		JsonTriggerActionGroupAction jact = new JsonTriggerActionGroupAction();
		jact.type = "triggeractiongroup";
		jact.actionGroupId = 9238298;
		
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/add", jact).statusCode, 400);
		
		jact.actionGroupId = actionGroupId;
		jact = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/action/add", jact, JsonTriggerActionGroupAction.class);
		
		assertTrue(jact.id > 0);
		actionId2 = jact.id;
	}
	
	@Test(dependsOnMethods="testAddTriggerActionGroupAction")
	public void testAddSendMailAction() {
		JsonSendMailAction jact = new JsonSendMailAction();
		jact.type = "sendmail";
		
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/add", jact).statusCode, 400);
		
		jact.subject = "Ett ämne";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/add", jact).statusCode, 400);
		
		jact.recipients = "en@mottagare.se";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/add", jact).statusCode, 400);
		
		jact.text = "En text som ska stå som body i mailet.";
		jact = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/action/add", jact, JsonSendMailAction.class);
		
		assertTrue(jact.id > 0);
		actionId3 = jact.id;
	}
	
	@Test(dependsOnMethods="testAddSendMailAction")
	public void testList() {
		JsonAction[] jactions = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/action/list", JsonAction[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonAction jc : jactions)
			existingIds.add(jc.id);
		
		assertTrue(existingIds.contains(actionId1));
		assertTrue(existingIds.contains(actionId2));
		assertTrue(existingIds.contains(actionId3));
	}
	
	@Test(dependsOnMethods="testList")
	public void testSetChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.type = "changechannelvalue";
		jact.deviceId = deviceId2;
		jact.value = 3;
		jact.channel = 2;
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/" + actionId1 + "/set", jact).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetChangeChannelValueAction")
	public void testSetSendMailAction() {
		JsonSendMailAction jact = new JsonSendMailAction();
		jact.type = "sendmail";
		jact.subject = "Ett annat ämne";
		jact.recipients = "enannan@mottagare.se";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/action/" + actionId3 + "/set", jact).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetSendMailAction")
	public void testGetChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/action/" + actionId1 + "/get", JsonChangeChannelValueAction.class);
		assertEquals((int)jact.channel, 2);
		assertEquals(jact.value.intValue(), 3);
		assertEquals((int)jact.deviceId, deviceId2);
	}
	
	@Test(dependsOnMethods="testGetChangeChannelValueAction")
	public void testGetSendMailAction() {
		JsonSendMailAction jact = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/action/" + actionId3 + "/get", JsonSendMailAction.class);
		assertEquals(jact.subject, "Ett annat ämne");
		assertEquals(jact.recipients, "enannan@mottagare.se");
		assertEquals(jact.text, "En text som ska stå som body i mailet.");
	}
	
	@Test(dependsOnMethods="testGetSendMailAction")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/delete").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/get").statusCode, 400);
	}
}
