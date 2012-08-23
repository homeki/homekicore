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
	private int conditionId;
	private int deviceId;
	
	public class JsonTrigger {
		public Integer id;
		public String name;
	}
	
	public class JsonCondition {
		public String type;
		public Integer id;
		public String shortDescription;
	}
	
	public class JsonChannelChangedCondition {
		public Integer id;
		public Integer deviceId;
		public Integer channel;
		public Number number;
		public String operator;
	}
	
	public class JsonMinuteChangedCondition {
		public int id;
		public String weekday;
		public String day;
		public Integer hour;
		public Integer minute;
		public String timeOperator;
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
		//TestUtil.deleteDevice(deviceId);
		// TODO: delete trigger
	}
	
	@Test
	public void testAddChannelChanged() {
		JsonChannelChangedCondition jcond = new JsonChannelChangedCondition();
		jcond.deviceId = deviceId;
		jcond.number = 12;
		jcond.channel = 1;
		jcond.operator = "dontthinkso";
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add?type=channelvalue", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" +  triggerId + "/condition/add?type=channelvalue", jcond).statusCode, 400);
		
		jcond.operator = "LT";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add?type=channelvalue", jcond, JsonChannelChangedCondition.class);
		
		assertTrue(jcond.id > 0);
	}
	
	@Test
	public void testAddMinuteChanged() {
		JsonMinuteChangedCondition jcond = new JsonMinuteChangedCondition();
		jcond.day = "1,13";
		jcond.weekday = "1,3,5";
		jcond.hour = 12;
		jcond.minute = 13;
		jcond.timeOperator = "naha";
		
		assertEquals(TestUtil.sendPost("/trigger/9999/condition/add?type=minute", jcond).statusCode, 400);
		assertEquals(TestUtil.sendPost("/trigger/" + triggerId + "/condition/add?type=minute", jcond).statusCode, 400);
		
		jcond.timeOperator = "EQ";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/" + triggerId + "/condition/add?type=minute", jcond, JsonMinuteChangedCondition.class);
		
		assertTrue(jcond.id > 0);
		conditionId = jcond.id;
	}
	
	@Test
	public void testList() {
		JsonCondition[] jconditions = TestUtil.sendGetAndParseAsJson("/trigger/" + triggerId + "/condition/list", JsonCondition[].class);
		
		Set<Integer> existingIds = new HashSet<Integer>();
		
		for (JsonCondition jc : jconditions)
			existingIds.add(jc.id);
		
		assertTrue(existingIds.contains(conditionId));
	}
}
