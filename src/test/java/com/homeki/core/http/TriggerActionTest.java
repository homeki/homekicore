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
		public Integer triggerId;
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
		jtrigger = TestUtil.sendPostAndParseAsJson("/triggers", jtrigger, JsonTrigger.class);
		triggerId = jtrigger.triggerId;
		
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
		TestUtil.sendDelete("/triggers/" + triggerId);
		TestUtil.sendGet("/actiongroup/" + actionGroupId + "/delete");
	}
	
	@Test
	public void testAddChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.deviceId = deviceId1;
		jact.value = 1;
		jact.channel = 1;

		jact.type = "changechannelvalue";
		assertEquals(TestUtil.sendPost("/triggers/9999/actions", jact).statusCode, 400);

		jact.type = "feeelchangechannelvalue";
		assertEquals(TestUtil.sendPost("/triggers/" +  triggerId + "/actions", jact).statusCode, 400);

		jact.type = "changechannelvalue";
		jact = TestUtil.sendPostAndParseAsJson("/triggers/" + triggerId + "/actions", jact, JsonChangeChannelValueAction.class);
		
		assertTrue(jact.actionId > 0);
		actionId1 = jact.actionId;
	}
	
	@Test(dependsOnMethods="testAddChangeChannelValueAction")
	public void testAddTriggerActionGroupAction() {
		JsonTriggerActionGroupAction jact = new JsonTriggerActionGroupAction();
		jact.type = "triggeractiongroup";
		jact.actionGroupId = 9238298;
		
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId + "/actions", jact).statusCode, 400);
		
		jact.actionGroupId = actionGroupId;
		jact = TestUtil.sendPostAndParseAsJson("/triggers/" + triggerId + "/actions", jact, JsonTriggerActionGroupAction.class);
		
		assertTrue(jact.actionId > 0);
		actionId2 = jact.actionId;
	}
	
	@Test(dependsOnMethods="testAddTriggerActionGroupAction")
	public void testAddSendMailAction() {
		JsonSendMailAction jact = new JsonSendMailAction();
		jact.type = "sendmail";
		
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId + "/actions", jact).statusCode, 400);
		
		jact.subject = "Ett ämne";
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId + "/actions", jact).statusCode, 400);
		
		jact.recipients = "en@mottagare.se";
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId + "/actions", jact).statusCode, 400);
		
		jact.text = "En text som ska stå som body i mailet.";
		jact = TestUtil.sendPostAndParseAsJson("/triggers/" + triggerId + "/actions", jact, JsonSendMailAction.class);
		
		assertTrue(jact.actionId > 0);
		actionId3 = jact.actionId;
	}
	
	@Test(dependsOnMethods="testAddSendMailAction")
	public void testList() {
		JsonAction[] jactions = TestUtil.sendGetAndParseAsJson("/triggers/" + triggerId + "/actions", JsonAction[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonAction jc : jactions)
			existingIds.add(jc.actionId);
		
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
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId + "/actions/" + actionId1, jact).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetChangeChannelValueAction")
	public void testSetSendMailAction() {
		JsonSendMailAction jact = new JsonSendMailAction();
		jact.type = "sendmail";
		jact.subject = "Ett annat ämne";
		jact.recipients = "enannan@mottagare.se";
		assertEquals(TestUtil.sendPost("/triggers/" + triggerId + "/actions/" + actionId3, jact).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetSendMailAction")
	public void testGetChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = TestUtil.sendGetAndParseAsJson("/triggers/" + triggerId + "/actions/" + actionId1, JsonChangeChannelValueAction.class);
		assertEquals((int)jact.channel, 2);
		assertEquals(jact.value.intValue(), 3);
		assertEquals((int)jact.deviceId, deviceId2);
	}
	
	@Test(dependsOnMethods="testGetChangeChannelValueAction")
	public void testGetSendMailAction() {
		JsonSendMailAction jact = TestUtil.sendGetAndParseAsJson("/triggers/" + triggerId + "/actions/" + actionId3, JsonSendMailAction.class);
		assertEquals(jact.subject, "Ett annat ämne");
		assertEquals(jact.recipients, "enannan@mottagare.se");
		assertEquals(jact.text, "En text som ska stå som body i mailet.");
	}
	
	@Test(dependsOnMethods="testGetSendMailAction")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/triggers/" + triggerId + "/actions/" + actionId1).statusCode, 200);
		assertEquals(TestUtil.sendDelete("/triggers/" + triggerId + "/actions/" + actionId1).statusCode, 200);
		assertEquals(TestUtil.sendGet("/triggers/" + triggerId + "/actions/" + actionId1).statusCode, 400);
	}
}
