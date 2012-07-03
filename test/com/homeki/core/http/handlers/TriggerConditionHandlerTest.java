package com.homeki.core.http.handlers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.homeki.core.TestUtil;
import com.homeki.core.http.json.JsonCondition;

public class TriggerConditionHandlerTest {
	private int triggerId;
	
	public class JsonTrigger {
		public Integer id;
		public String name;
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
	}
	
	@AfterClass
	public void afterClass() {
		// TODO: delete trigger
	}
	
	@Test
	public void testAddChannelChanged() {
		JsonChannelChangedCondition jcond = new JsonChannelChangedCondition();
		jcond.deviceId = 10;
		jcond.number = 12;
		jcond.channel = 1;
		jcond.operator = "dontthinkso";
		
		assertEquals(405, TestUtil.sendPost("/trigger/condition/add?triggerid=9999&type=channelchanged", jcond).statusCode);
		assertEquals(405, TestUtil.sendPost("/trigger/condition/add?triggerid=" + triggerId + "&type=channelchanged", jcond).statusCode);
		
		jcond.operator = "LT";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/condition/add?triggerid=" + triggerId + "&type=channelchanged", jcond, JsonChannelChangedCondition.class);
		
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
		
		assertEquals(405, TestUtil.sendPost("/trigger/condition/add?triggerid=9999&type=minutechanged", jcond).statusCode);
		assertEquals(405, TestUtil.sendPost("/trigger/condition/add?triggerid=" + triggerId + "&type=minutechanged", jcond).statusCode);
		
		jcond.timeOperator = "EQ";
		
		jcond = TestUtil.sendPostAndParseAsJson("/trigger/condition/add?triggerid=" + triggerId + "&type=minutechanged", jcond, JsonMinuteChangedCondition.class);
		
		assertTrue(jcond.id > 0);
	}
}
