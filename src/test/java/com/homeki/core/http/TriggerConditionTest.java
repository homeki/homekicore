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

public class TriggerConditionTest {
	private int triggerId;
	private int conditionId1;
	private int conditionId2;
	private int conditionId3;
	private int conditionId4;
	private int deviceId;
	
	public static class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	public static class JsonCondition {
		public String type;
		public Integer id;
	}
	
	public static class JsonChannelValueCondition extends JsonCondition {
		public Integer deviceId;
		public Integer channel;
		public Number value;
		public String operator;
	}
	
	public static class JsonSpecialValueCondition extends JsonCondition {
		public String source;
		public Integer value;
		public String operator;
		public Boolean customSource;
	}
	
	public static class JsonMinuteCondition extends JsonCondition {
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
	public void testAddChannelValueCondition() {
		JsonChannelValueCondition jcond = new JsonChannelValueCondition();
		jcond.type = "channelvalue";
		jcond.deviceId = deviceId;
		jcond.value = 12;
		jcond.channel = 1;
		jcond.operator = "dontthinkso";
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/condition/add", jcond).statusCode, 400);
		
		jcond.operator = "LT";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add", jcond, JsonChannelValueCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId2 = jcond.id;
	}
	
	@Test(dependsOnMethods="testAddChannelValueCondition")
	public void testAddMinuteCondition() {
		JsonMinuteCondition jcond = new JsonMinuteCondition();
		jcond.type = "minute";
		jcond.day = "1,13";
		jcond.weekday = "";
		jcond.hour = 12;
		jcond.minute = 13;
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add", jcond).statusCode, 400);

		jcond.type = "feeelminute";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/add", jcond).statusCode, 400);

		jcond.type = "minute";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/add", jcond).statusCode, 400);
		
		jcond.weekday = "*";
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add", jcond, JsonMinuteCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId1 = jcond.id;
	}
	
	@Test(dependsOnMethods="testAddMinuteCondition")
	public void testAddSpecialValueCondition() {
		JsonSpecialValueCondition jcond = new JsonSpecialValueCondition();
		jcond.type = "specialvalue";
		jcond.value = 12;
		jcond.source = "123CONNEadCTED_CLIENasdTS";
		jcond.operator = "dontthinkso";
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/condition/add", jcond).statusCode, 400);
		
		jcond.operator = "LT";
		
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/condition/add", jcond).statusCode, 400);
		
		jcond.source = "CONNECTED_CLIENTS";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add", jcond, JsonSpecialValueCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId3 = jcond.id;

		jcond.type = "specialvalue";
		jcond.operator = "EQ";
		jcond.value = 12;
		jcond.source = "SOME_CUSTOM_SOURCE";
		jcond.customSource = true;
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add", jcond, JsonSpecialValueCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId4 = jcond.id;
	}
	
	@Test(dependsOnMethods="testAddSpecialValueCondition")
	public void testList() {
		JsonCondition[] jconditions = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/list", JsonCondition[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonCondition jc : jconditions)
			existingIds.add(jc.id);
		
		assertTrue(existingIds.contains(conditionId1));
		assertTrue(existingIds.contains(conditionId2));
		assertTrue(existingIds.contains(conditionId3));
		assertTrue(existingIds.contains(conditionId4));
	}

	@Test(dependsOnMethods="testList")
	public void testSetChannelValueCondition() {
		JsonChannelValueCondition jcond = new JsonChannelValueCondition();
		jcond.type = "channelvalue";
		jcond.value = 13;
		jcond.channel = 2;
		jcond.operator = "GT";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/" + conditionId2 + "/set", jcond).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetChannelValueCondition")
	public void testSetMinuteCondition() {
		JsonMinuteCondition jcond = new JsonMinuteCondition();
		jcond.type = "minute";
		jcond.weekday = "4";
		jcond.hour = 13;
		jcond.minute = 14;
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/" + conditionId1 + "/set", jcond).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetMinuteCondition")
	public void testSetSpecialValueCondition() {
		JsonSpecialValueCondition jcond = new JsonSpecialValueCondition();
		jcond.type = "specialvalue";
		jcond.value = 99;
		jcond.operator = "GT";
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/" + conditionId3 + "/set", jcond).statusCode, 200);
	}
	
	@Test(dependsOnMethods="testSetSpecialValueCondition")
	public void testGetMinuteCondition() {
		JsonMinuteCondition jcond = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/" + conditionId1 + "/get", JsonMinuteCondition.class);
		assertEquals(jcond.day, "1,13");
		assertEquals(jcond.weekday, "4");
		assertEquals((int)jcond.hour, 13);
		assertEquals((int)jcond.minute, 14);
	}
	
	@Test(dependsOnMethods="testGetMinuteCondition")
	public void testGetChannelValueCondition() {
		JsonChannelValueCondition jcond = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/" + conditionId2 + "/get", JsonChannelValueCondition.class);
		assertEquals((int)jcond.deviceId, deviceId);
		assertEquals(jcond.value.intValue(), 13);
		assertEquals((int)jcond.channel, 2);
		assertEquals(jcond.operator, "GT");
	}
	
	@Test(dependsOnMethods="testGetChannelValueCondition")
	public void testGetSpecialValueCondition() {
		JsonSpecialValueCondition jcond = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/" + conditionId3 + "/get", JsonSpecialValueCondition.class);
		assertEquals(jcond.source, "CONNECTED_CLIENTS");
		assertEquals((int)jcond.value, 99);
		assertEquals(jcond.operator, "GT");
	}
	
	@Test(dependsOnMethods="testGetSpecialValueCondition")
	public void testDelete() {
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/condition/" + conditionId1 + "/get").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/condition/" + conditionId1 + "/delete").statusCode, 200);
		assertEquals(TestUtil.sendGet("/trigger/" + triggerId + "/condition/" + conditionId1 + "/get").statusCode, 400);
	}
}
