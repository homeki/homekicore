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
	private int deviceId;
	
	public class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	public class JsonAction {
		public String type;
		public Integer id;
	}
	
	public class JsonChangeChannelValueAction {
		public Integer id;
		public Integer deviceId;
		public Integer channel;
		public Number value;
	}
	
	@BeforeClass
	public void beforeClass() {
		JsonTrigger jtrigger = new JsonTrigger();
		jtrigger.name = "foractiontest";
		jtrigger = TestUtil.sendPostAndParseAsJson("/trigger/add", jtrigger, JsonTrigger.class);
		triggerId = jtrigger.id;
		deviceId = TestUtil.addMockDevice(MockDeviceType.SWITCH);
	}
	
	@AfterClass
	public void afterClass() {
		TestUtil.deleteDevice(deviceId);
		TestUtil.sendGet("/trigger/" + triggerId + "/delete");
	}
	
	@Test
	public void testAddChangeChannelValueAction() {
		JsonChangeChannelValueAction jact = new JsonChangeChannelValueAction();
		jact.deviceId = deviceId;
		jact.value = 1;
		jact.channel = 1;
		
		assertEquals(TestUtil.sendPost("/trigger/9999/action/add?type=changechannelvalue", jact).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/action/add?type=feelchangechannelvalue", jact).statusCode, 400);
		
		jact = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/action/add?type=changechannelvalue", jact, JsonChangeChannelValueAction.class);
		
		assertTrue(jact.id > 0);
		actionId1 = jact.id;
	}
	
	@Test(dependsOnMethods="testAddChangeChannelValueAction")
	public void testList() {
		JsonAction[] jactions = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/action/list", JsonAction[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonAction jc : jactions)
			existingIds.add(jc.id);
		
		assertTrue(existingIds.contains(actionId1));
	}
	
	@Test(dependsOnMethods="testList")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/action/" + actionId1 + "/delete").statusCode, 200);
	}
}
