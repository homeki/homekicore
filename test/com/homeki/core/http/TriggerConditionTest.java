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

public class TriggerConditionTest {
	private int triggerId;
	private int conditionId1;
	private int conditionId2;
	private int deviceId;
	
	public class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	public class JsonCondition {
		public String type;
		public Integer id;
	}
	
	public class JsonChannelChangedCondition extends JsonCondition {
		public Integer deviceId;
		public Integer channel;
		public Number value;
		public String operator;
	}
	
	public class JsonMinuteCondition extends JsonCondition {
		public String weekday;
		public String day;
		public Integer hour;
		public Integer minute;
	}
	
	@BeforeClass
	public void beforeClass() {
		JsonTrigger jtrigger = new JsonTrigger();
		jtrigger.name = "forconditiontest";
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
	public void testAddChannelChangedCondition() {
		JsonChannelChangedCondition jcond = new JsonChannelChangedCondition();
		jcond.deviceId = deviceId;
		jcond.value = 12;
		jcond.channel = 1;
		jcond.operator = "dontthinkso";
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add?type=channelvalue", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/condition/add?type=channelvalue", jcond).statusCode, 400);
		
		jcond.operator = "LT";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add?type=channelvalue", jcond, JsonChannelChangedCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId2 = jcond.id;
	}
	
	@Test(dependsOnMethods="testAddChannelChangedCondition")
	public void testAddMinuteCondition() {
		JsonMinuteCondition jcond = new JsonMinuteCondition();
		jcond.day = "1,13";
		jcond.weekday = "";
		jcond.hour = 12;
		jcond.minute = 13;
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add?type=minute", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/add?type=feelminute", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/add?type=minute", jcond).statusCode, 400);
		
		jcond.weekday = "*";
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add?type=minute", jcond, JsonMinuteCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId1 = jcond.id;
	}
	
	@Test(dependsOnMethods="testAddMinuteCondition")
	public void testList() {
		JsonCondition[] jconditions = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/list", JsonCondition[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonCondition jc : jconditions)
			existingIds.add(jc.id);
		
		assertTrue(existingIds.contains(conditionId1));
		assertTrue(existingIds.contains(conditionId2));
	}

	@Test(dependsOnMethods="testList")
	public void testSetChannelChangedCondition() {
		JsonChannelChangedCondition jcond = new JsonChannelChangedCondition();
		jcond.value = 13;
		jcond.channel = 2;
		jcond.operator = "GT";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/" + conditionId2 + "/set", jcond).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetChannelChangedCondition")
	public void testSetMinuteCondition() {
		JsonMinuteCondition jcond = new JsonMinuteCondition();
		jcond.weekday = "4";
		jcond.hour = 13;
		jcond.minute = 14;
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/" + conditionId1 + "/set", jcond).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetMinuteCondition")
	public void testGetMinuteCondition() {
		JsonMinuteCondition jcond = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/" + conditionId1 + "/get", JsonMinuteCondition.class);
		assertEquals(jcond.day, "1,13");
		assertEquals(jcond.weekday, "4");
		assertEquals((int)jcond.hour, 13);
		assertEquals((int)jcond.minute, 14);
	}
	
	@Test(dependsOnMethods="testGetMinuteCondition")
	public void testGetChannelChangedCondition() {
		JsonChannelChangedCondition jcond = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/" + conditionId2 + "/get", JsonChannelChangedCondition.class);
		assertEquals((int)jcond.deviceId, deviceId);
		assertEquals(jcond.value, 13);
		assertEquals((int)jcond.channel, 2);
		assertEquals(jcond.operator, "GT");
	}
	
	@Test(dependsOnMethods="testGetChannelChangedCondition")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/condition/" + conditionId1 + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/condition/" + conditionId1 + "/delete").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/condition/" + conditionId1 + "/get").statusCode, 400);
	}
}
