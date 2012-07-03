package com.homeki.core.http.handlers;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.homeki.core.TestUtil;

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
}
